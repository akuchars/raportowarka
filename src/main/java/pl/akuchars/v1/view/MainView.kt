package pl.akuchars.v1.view

import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.DatePicker
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import pl.akuchars.v1.controller.TimeSheetController
import pl.akuchars.v1.model.SendWorklogActionForm
import pl.akuchars.v1.model.WorklogsForm
import tornadofx.*
import java.awt.Desktop
import java.net.URI
import java.time.LocalDate
import java.time.LocalDate.now
import java.time.temporal.TemporalAdjusters.firstDayOfMonth
import java.time.temporal.TemporalAdjusters.lastDayOfMonth


class MainView : View("Raportowarka v2") {
	// todo pl.akuchars przerobić na fxml
	override val root = BorderPane()

	private var result: Label by singleAssign()
	private var overtimesPeriodResult: Label by singleAssign()
	private var start: DatePicker by singleAssign()
	private var end: DatePicker by singleAssign()
	private var production: CheckBox by singleAssign()
	private var honest: CheckBox by singleAssign()
	private var sendBtn: Button by singleAssign()
	private var sendBtnToday: Button by singleAssign()

	private val controller: TimeSheetController by inject()

	init {
		with(root) {
			right {
				form {
					fieldset("Zaraportuj czas w podanym przedziale") {
						field("Data początkowa") {
							start = datepicker()
							start.setOnAction {
								showWorklogForCurrentPeriod()
							}
						}
						field("Data końcowa") {
							end = datepicker()
							end.setOnAction {
								showWorklogForCurrentPeriod()
							}
						}
						field("Produkcja") {
							production = checkbox()
						}
						field("Uczciwy") {
							honest = checkbox()
						}
						field("Rezultat") {
							result = label()
							result.text = "Załadowano widok"
						}
						field("Zaraportowano:") {
							overtimesPeriodResult = label()
							createReportedActualResult()
						}
						button("Wyślij").apply {
							sendBtn = this
						}.action {
							sendWorklogToTimeSheet()
						}
						button("Wyślij na dzień dzisiejszy").apply {
							sendBtnToday = this
						}.action {
							sendWorklogToTimeSheetToday()
						}
						hyperlink(
							text = "Tempo in jira"
						).action {
							val url = JIRA_TEMPO_URL.format(now().with(firstDayOfMonth()), now().with(lastDayOfMonth()))
							if (Desktop.isDesktopSupported()) {
								kotlin.runCatching { Desktop.getDesktop().browse(URI(url)) }
							}
						}
					}
				}
			}
		}
	}

	private fun createReportedActualResult() {
		val reportedTime = controller.hours(
			WorklogsForm("akuchars", now().with(firstDayOfMonth()), now())
		)
		overtimesPeriodResult.text = "Normalny czas: ${reportedTime.time} / ${reportedTime.requiredHours}  " +
			"\nNadgodziny: ${reportedTime.overtime}"
	}

	private fun showWorklogForCurrentPeriod() {
		val startDate = start.valueProperty()?.value
		val endDate = end.valueProperty()?.value
		if (startDate != null && endDate != null) {
			result.text = "Rozpoczęto zadanie. Czekaj..."
			runAsync {
				// todo pl.akuchars wyświetlić informacje w tabelce
				controller.worklogsForPeriod(SendWorklogActionForm(startDate, endDate))
			}
		}
	}

	private fun sendWorklogToTimeSheetToday() {
		sendWorklogInner(now(), now())
	}

	private fun sendWorklogToTimeSheet() {
		val startDate = start.valueProperty().value
		val endDate = end.valueProperty().value
		sendWorklogInner(startDate, endDate)
	}

	private fun sendWorklogInner(startDate: LocalDate, endDate: LocalDate) {
		result.text = "Rozpoczęto zadanie. Czekaj..."
		sendBtn.isDisable = true
		sendBtnToday.isDisable = true
		runAsync {
			// todo pl.akuchars znaleźć sposób na loggera (podwójne bindowanie)?
			controller.createForPeriod(
				SendWorklogActionForm(
					startDate, endDate.plusDays(1L),
					production.isSelected,
					honest.isSelected
				)
			)
		}.setOnSucceeded {
			result.text = "Zadanie zakończone"
			sendBtn.isDisable = false
			sendBtnToday.isDisable = false
			createReportedActualResult()
		}
	}

	companion object {
		private const val JIRA_TEMPO_URL = "https://jira.unity.pl/secure/Tempo.jspa#/my-work/timesheet?" +
			"columns=WORKED_COLUMN" +
			"&columns=PLAN_COLUMN&dateDisplayType=days" +
			"&from=%s" +
			"&groupBy=project" +
			"&groupBy=issue" +
			"&periodType=CURRENT_PERIOD" +
			"&subPeriodType=MONTH" +
			"&to=%s" +
			"&viewType=TIMESHEET" +
			"&worker=akuchars"
	}

}