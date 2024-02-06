package pl.akuchars.v1.jira

import pl.akuchars.v1.jira.timesheets.TimesheetRestClient
import pl.akuchars.v1.jira.timesheets.dto.OvertimeAttributeDto
import pl.akuchars.v1.jira.timesheets.dto.WorkLogDto
import pl.akuchars.v1.model.WorklogsForm
import java.time.LocalDate

class Main {

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {

			val jiraClient = createWithExtraClients(JiraCredential())
			val timesheetClient: TimesheetRestClient = jiraClient.timesheetClient

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
	}
}


