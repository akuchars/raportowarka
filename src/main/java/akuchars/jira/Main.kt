package akuchars.jira

import akuchars.jira.timesheets.TimesheetRestClient
import akuchars.jira.timesheets.dto.WorkLogDto
import akuchars.jira.timesheets.dto.worklogs.WorklogsForm
import java.time.LocalDate

class Main {

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {

			val jiraClient = createWithExtraClients(JiraCredential())
			val timesheetClient: TimesheetRestClient = jiraClient.timesheetClient

			val createWorkLog = timesheetClient.createWorkLog(
				WorkLogDto(
					"akuchars",
					"Raportowanie czasu",
					"2020-11-09",
					"2020-11-09",
					60,
					null,
					"PIT-1317",
					0,
					true,
					null
				)
			)

			val worklogs = timesheetClient.worklogs(WorklogsForm("akuchars", LocalDate.of(2022, 3, 1), LocalDate.of(2022, 3, 31))).claim()

			println(worklogs.toOvertime().countTime())

		}
	}
}


