package pl.akuchars.v1.jira.timesheets

import pl.akuchars.v1.jira.timesheets.dto.WorkLogDto
import pl.akuchars.v1.jira.timesheets.dto.worklogs.SchedulePeriods
import pl.akuchars.v1.jira.timesheets.dto.worklogs.Worklogs
import pl.akuchars.v1.model.WorklogsForm
import com.atlassian.util.concurrent.Promise

interface TimesheetRestClient {

	fun createWorkLog(workLog: WorkLogDto): Promise<Void>

	fun worklogs(form: WorklogsForm): Promise<Worklogs>
	fun userSchedule(form: WorklogsForm): Promise<SchedulePeriods>
}