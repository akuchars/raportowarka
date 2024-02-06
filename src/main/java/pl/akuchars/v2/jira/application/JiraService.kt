package pl.akuchars.v2.jira.application

import com.atlassian.jira.rest.client.api.JiraRestClient
import com.google.gson.Gson
import org.springframework.stereotype.Service
import pl.akuchars.v1.controller.SendWorklogActionCommand
import pl.akuchars.v1.kernel.HoursMinutes
import pl.akuchars.v1.model.WorklogsForm
import pl.akuchars.v2.jira.application.dto.OvertimeAttributeDto
import pl.akuchars.v2.jira.application.dto.WorkLogDto
import pl.akuchars.v2.jira.application.dto.worklogs.WorklogsHoursMinutes
import pl.akuchars.v2.jira.domain.JiraCredential
import pl.akuchars.v2.jira.infrastructure.timesheetClient
import pl.akuchars.v2.jtoggl.domain.DayTimeEntry

@Service
class JiraService internal constructor(
    private val jiraClient: JiraRestClient,
    private val jiraCredential: JiraCredential,
    private val estimateResolver: RemainingEstimateResolver
) {
    fun getHours(form: WorklogsForm): WorklogsHoursMinutes {
        val userSchedulePeriods  = jiraClient.timesheetClient.userSchedule(form).claim()
        return jiraClient.timesheetClient.worklogs(form).claim().countTime().apply {
            this.requiredHours = HoursMinutes(userSchedulePeriods.requiredSeconds!!.toLong())
        }
    }

    // TODO całe DayTimeEntry nie może być przekazywane!!! bo to domena
    fun createWorkLogDto(timeEntriesByDay: List<DayTimeEntry>): List<WorkLogDto> {
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
                    estimateResolver.invoke(information),
                    false,
                    if (information.isOvertimeEntry) OvertimeAttributeDto() else null
                )
            }
        }.also(::print)
    }

    fun tryToSendWorkLogToJira(command: SendWorklogActionCommand, dto: WorkLogDto) {
        if (command.production()) {
            kotlin.runCatching {
                jiraClient.timesheetClient.createWorkLog(dto).claim()
            }.onFailure {
                println("Jakiś problem podczas wysyłania dla: $dto, worklog zostanie zignorowany")
            }
        }
    }


    private fun print(workLogsDto: List<WorkLogDto>) {
        val toJson = Gson().toJson(workLogsDto[0])
        println("Try to update worklogs: ${Gson().toJson(workLogsDto)}")
        println(toJson)
    }

}