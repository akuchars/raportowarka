package akuchars.jira.timesheets

import akuchars.jira.timesheets.dto.WorkLogDto
import akuchars.jira.timesheets.dto.worklogs.Worklogs
import akuchars.jira.timesheets.dto.worklogs.WorklogsForm
import com.atlassian.util.concurrent.Promise

interface TimesheetRestClient {

	fun createWorkLog(workLog: WorkLogDto): Promise<Void>

	fun worklogs(form: WorklogsForm): Promise<Worklogs>
}