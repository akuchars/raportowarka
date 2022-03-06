package akuchars.view

import akuchars.controller.TimeSheetController
import akuchars.jira.timesheets.dto.worklogs.WorklogsForm
import akuchars.kernel.HoursMinutes
import akuchars.model.SendWorklogActionForm
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.DatePicker
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import tornadofx.*
import java.awt.Desktop
import java.net.URI
import java.time.LocalDate
import java.time.LocalDate.now
import java.time.temporal.TemporalAdjusters.firstDayOfMonth
import java.time.temporal.TemporalAdjusters.lastDayOfMonth


class MainView : View("Raportowarka v2") {
	// todo akuchars przerobić na fxml/vaddin
	override val root = BorderPane()

	private var result: Label by singleAssign()
	private var overtimes1WeekResult: Label by singleAssign()
	private var overtimes2WeekResult: Label by singleAssign()
	private var overtimes3WeekResult: Label by singleAssign()
	private var overtimes4WeekResult: Label by singleAssign()
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
							overtimes1WeekResult = label()
							createReportedWeekResult(
								now().with(firstDayOfMonth()),
								now(),
								overtimes1WeekResult
							)
						}
//						field("Zaraportowano (2 tydzień): ") {
//							overtimes2WeekResult = label()
//							createReportedWeekResult(
//								now().with(firstDayOfMonth()).plusDays(7L),
//								now().with(firstDayOfMonth()).plusDays(13L),
//								overtimes2WeekResult
//							)
//						}
//						field("Zaraportowano (3 tydzień): ") {
//							overtimes3WeekResult = label()
//							createReportedWeekResult(
//								now().with(firstDayOfMonth()).plusDays(14L),
//								now().with(firstDayOfMonth()).plusDays(20L),
//								overtimes3WeekResult
//							)
//						}
//						field("Zaraportowano (4 tydzień): ") {
//							overtimes4WeekResult = label()
//							createReportedWeekResult(
//								now().with(firstDayOfMonth()).plusDays(21L),
//								now().with(lastDayOfMonth()),
//								overtimes4WeekResult
//							)
//						}
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

	private fun createReportedWeekResult(startDate: LocalDate, endDate: LocalDate, weekLabel: Label) {
		val reportedTime = controller.hours(
			WorklogsForm("akuchars", startDate, endDate)
		)
		// todo akuchars (FEATURE) --- dodać faktyczny czas potrzebny do zaraportowania danego miesiąca
		//  dni wolne mogę wziąć tutaj: https://apidocs.tempo.io/#user_schedule
		weekLabel.text = "Normalny czas: ${reportedTime.time} / 40 H  \nNadgodziny: ${reportedTime.overtime}"
	}

	private fun showWorklogForCurrentPeriod() {
		val startDate = start.valueProperty()?.value
		val endDate = end.valueProperty()?.value
		if (startDate != null && endDate != null) {
			result.text = "Rozpoczęto zadanie. Czekaj..."
			runAsync {
				// todo akuchars wyświetlić informacje w tabelce
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
			// todo akuchars znaleźć sposób na loggera (podwójne bindowanie)?
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
			createReportedWeekResult(
				now().with(firstDayOfMonth()),
				now(),
				overtimes1WeekResult
			)
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