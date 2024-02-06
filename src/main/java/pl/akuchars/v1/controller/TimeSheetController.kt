package pl.akuchars.v1.controller

import com.google.gson.Gson
import pl.akuchars.v1.jira.JiraCredential
import pl.akuchars.v1.jira.createWithExtraClients
import pl.akuchars.v1.jira.timesheetClient
import pl.akuchars.v1.jira.timesheets.TimesheetRestClient
import pl.akuchars.v1.jira.timesheets.dto.OvertimeAttributeDto
import pl.akuchars.v1.jira.timesheets.dto.WorkLogDto
import pl.akuchars.v1.model.WorklogsForm
import pl.akuchars.v1.jira.timesheets.dto.worklogs.WorklogsHoursMinutes
import pl.akuchars.v1.jtoggl.*
import pl.akuchars.v1.kernel.HoursMinutes
import kotlin.math.max

class TimeSheetController : tornadofx.Controller() {
	// todo pl.akuchars tutaj to powinno być wstrzyknięte
	private val togglCredential: TogglCredential = TogglCredential()
	private val jiraCredential: JiraCredential = JiraCredential()
	private val jiraClient = createWithExtraClients(jiraCredential)

	fun worklogsForPeriod(command: SendWorklogActionCommand): PeriodTimeEntry =
		PeriodTimeEntry(getTooglWorkLogs(command, togglCredential.apiToken))

	fun hours(form: WorklogsForm): WorklogsHoursMinutes {
		val userSchedulePeriods  = jiraClient.timesheetClient.userSchedule(form).claim()
		return jiraClient.timesheetClient.worklogs(form).claim().countTime().apply {
			this.requiredHours = HoursMinutes(userSchedulePeriods.requiredSeconds!!.toLong())
		}
	}

	fun createForPeriod(command: SendWorklogActionCommand) {
		val timeEntriesByDay = getTooglWorkLogs(command, togglCredential.apiToken)

		val workLogsDto = mapToJiraWorkLog(timeEntriesByDay) { timeEntry ->
			val currentRemaining = (jiraClient.issueClient.getIssue(timeEntry.key)
				.fail { System.err.println("Cannot find issue by key: ${timeEntry.key}") }
				.claim()
				.timeTracking?.remainingEstimateMinutes ?: 0) * 60
			max((currentRemaining - timeEntry.hoursMinutes.duration / 60), 0).toInt()
		}
		print(workLogsDto)

		if (command.production()) {
			workLogsDto.forEach {
				tryToSendWorklogToJira(jiraClient.timesheetClient, it)
			}
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