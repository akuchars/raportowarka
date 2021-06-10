package akuchars.jira

import akuchars.jira.timesheets.TimesheetRestClient
import akuchars.jira.timesheets.dto.WorkLogDto

class Main {

	companion object {
		@JvmStatic
		fun main(args: Array<String>) {

			val jiraClient = createWithExtraClients(JiraCredential())
			val timesheetClient: TimesheetRestClient = jiraClient.timesheetClient

			val createWorkLog = timesheetClient.createWorkLog(WorkLogDto(
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
			))

			val claim = jiraClient.issueClient.getIssue("VGPORD-323").claim()
			claim.affectedVersions

		}
	}
}


