package akuchars.controller

import akuchars.jira.JiraCredential
import akuchars.jira.createWithExtraClients
import akuchars.jira.timesheetClient
import akuchars.jira.timesheets.TimesheetRestClient
import akuchars.jira.timesheets.dto.OvertimeAttributeDto
import akuchars.jira.timesheets.dto.WorkLogDto
import akuchars.jira.timesheets.dto.worklogs.WorklogsForm
import akuchars.jira.timesheets.dto.worklogs.WorklogsHoursMinutes
import akuchars.jtoggl.*
import akuchars.kernel.HoursMinutes
import com.google.gson.Gson
import kotlin.math.max

class TimeSheetController : tornadofx.Controller() {
	// todo akuchars tutaj to powinno być wstrzyknięte
	// todo akuchars przerobić na fasadę, żeby można było użyć inne
	private val tooglCredential: TooglCredential = TooglCredential()
	private val jiraCredential: JiraCredential = JiraCredential()
	private val jiraClient = createWithExtraClients(jiraCredential)

	fun worklogsForPeriod(command: SendWorklogActionCommand): PeriodTimeEntry =
		PeriodTimeEntry(getTooglWorkLogs(command, tooglCredential.apiToken))

	fun hours(form: WorklogsForm): WorklogsHoursMinutes {
		val userSchedulePeriods  = jiraClient.timesheetClient.userSchedule(form).claim()
		return jiraClient.timesheetClient.worklogs(form).claim().countTime().apply {
			this.requiredHours = HoursMinutes(userSchedulePeriods.requiredSeconds!!.toLong())
		}
	}

	fun createForPeriod(command: SendWorklogActionCommand) {
		val timeEntriesByDay = getTooglWorkLogs(command, tooglCredential.apiToken)

		val workLogsDto = mapToJiraWorkLog(timeEntriesByDay) { timeEntry ->
			val currentRemaining = (jiraClient.issueClient.getIssue(timeEntry.key)
				.fail { System.err.println("Cannot find issue by key: ${timeEntry.key}") }
				.claim()
				.timeTracking?.remainingEstimateMinutes ?: 0) * 60
			max((currentRemaining - timeEntry.hoursMinutes.duration) / 60, 0).toInt()
		}
		print(workLogsDto)

		if (command.production()) {
			workLogsDto.forEach {
				tryToSendWorklogToJira(jiraClient.timesheetClient, it)
			}
			// todo akuchars zwrócić które udało się zaraportować a które nie
			println("Wysłano informacje do jiry")
		}

		println("Finished")
	}

	private fun tryToSendWorklogToJira(timesheetClient: TimesheetRestClient, it: WorkLogDto) {
		val worklog = timesheetClient.createWorkLog(it)
		try {
			worklog.claim()
		} catch (e: Exception) {
			println("Jakiś problem podczas wysyłania dla: $it, worklog zostanie zignorowany")
		}
	}

	private fun print(workLogsDto: List<WorkLogDto>) {
		val toJson = Gson().toJson(workLogsDto[0])
		println("Try to update worklogs: ${Gson().toJson(workLogsDto)}")
		println(toJson)
	}

	private fun mapToJiraWorkLog(
		timeEntriesByDay: List<DayTimeEntry>,
		resolveRemainingEstimate: (TimeEntryInformation) -> Int
	): List<WorkLogDto> {
		return timeEntriesByDay.flatMap { dayTimeEntry ->
			dayTimeEntry.workingEntries.map { information ->
				WorkLogDto(
					jiraCredential.user,
					information.description.values(),
					dayTimeEntry.day.toString(),
					dayTimeEntry.day.toString(),
					information.hoursMinutes.duration.toInt(),
					null,
					information.key,
					resolveRemainingEstimate.invoke(information),
					false,
					if (information.isOvertimeEntry) OvertimeAttributeDto() else null
				)
			}
		}
	}

	private fun getTooglWorkLogs(command: SendWorklogActionCommand, togglApiToken: String): List<DayTimeEntry> {
		val jToggl = JTogglClient(togglApiToken)
		return jToggl.timeEntriesByDay(command.startDate(), command.endDate()).onEach {
			if (!command.honnest()) {
				it.fixToWorkHour()
			}
			if (it.workingEntriesWithNoKey.isNotEmpty())
				println("There is a problem to resolve key for: ${it.day}: ${it.workingEntriesWithNoKey}")
		}
	}
}