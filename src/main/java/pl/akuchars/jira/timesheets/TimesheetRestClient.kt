package pl.akuchars.jira.timesheets

import pl.akuchars.jira.timesheets.dto.WorkLogDto
import pl.akuchars.jira.timesheets.dto.worklogs.SchedulePeriods
import pl.akuchars.jira.timesheets.dto.worklogs.Worklogs
import pl.akuchars.model.WorklogsForm
import com.atlassian.util.concurrent.Promise

interface TimesheetRestClient {

	fun createWorkLog(workLog: WorkLogDto): Promise<Void>

	fun worklogs(form: WorklogsForm): Promise<Worklogs>
	fun userSchedule(form: WorklogsForm): Promise<SchedulePeriods>
}