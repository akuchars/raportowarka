package akuchars.jira.timesheets

import akuchars.jira.timesheets.dto.WorkLogDto
import com.atlassian.util.concurrent.Promise

interface TimesheetRestClient {

	fun createWorkLog(workLog: WorkLogDto): Promise<Void>
}