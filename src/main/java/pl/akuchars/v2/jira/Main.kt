package pl.akuchars.v2.jira

import com.atlassian.jira.rest.client.api.JiraRestClient
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import pl.akuchars.v1.model.WorklogsForm
import pl.akuchars.v2.jira.application.dto.OvertimeAttributeDto
import pl.akuchars.v2.jira.application.dto.WorkLogDto
import pl.akuchars.v2.jira.infrastructure.timesheetClient
import java.time.LocalDate

@SpringBootApplication
class Main

fun main(args: Array<String>) {
    val app = SpringApplication.run(Main::class.java, *args)

    val jiraClient: JiraRestClient = app.getBean(JiraRestClient::class.java)
    val timesheetClient = jiraClient.timesheetClient

    val worklog = timesheetClient.createWorkLog(
        WorkLogDto(
            "akuchars",
            "Raportowanie czasu",
            "2022-03-06",
            "2022-03-06",
            60,
            null,
            "PIT-1317",
            0,
            true,
//					null
            OvertimeAttributeDto()
        )
    )

    timesheetClient.userSchedule(
        WorklogsForm(
            "akuchars",
            LocalDate.of(2022, 3, 1),
            LocalDate.of(2022, 3, 10)
        )
    ).claim()
//			worklog.claim()
//			val worklogs = timesheetClient.worklogs(WorklogsForm("pl.akuchars", LocalDate.of(2022, 3, 1), LocalDate.of(2022, 3, 31))).claim()
//
//			println(worklogs.countTime())

}