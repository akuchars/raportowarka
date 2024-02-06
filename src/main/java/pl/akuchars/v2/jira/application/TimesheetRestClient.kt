package pl.akuchars.v2.jira.application

import pl.akuchars.v2.jira.application.dto.WorkLogDto
import pl.akuchars.v2.jira.application.dto.worklogs.SchedulePeriods
import pl.akuchars.v2.jira.application.dto.worklogs.Worklogs
import pl.akuchars.v1.model.WorklogsForm
import com.atlassian.util.concurrent.Promise

interface TimesheetRestClient {

	fun createWorkLog(workLog: WorkLogDto): Promise<Void>
	fun worklogs(form: WorklogsForm): Promise<Worklogs>
	fun userSchedule(form: WorklogsForm): Promise<SchedulePeriods>
}