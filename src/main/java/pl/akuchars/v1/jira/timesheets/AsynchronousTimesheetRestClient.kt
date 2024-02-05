package pl.akuchars.v1.jira.timesheets

import pl.akuchars.v1.jira.timesheets.dto.WorkLogDto
import pl.akuchars.v1.jira.timesheets.dto.worklogs.SchedulePeriods
import pl.akuchars.v1.jira.timesheets.dto.worklogs.Worklogs
import pl.akuchars.v1.model.WorklogsForm
import com.atlassian.httpclient.api.HttpClient
import com.atlassian.jira.rest.client.internal.async.AbstractAsynchronousRestClient
import com.atlassian.util.concurrent.Promise
import java.net.URI
import javax.ws.rs.core.UriBuilder

//https://www.tempo.io/server-api-documentation
class AsynchronousTimesheetRestClient(httpClient: HttpClient, serverUri: URI) : AbstractAsynchronousRestClient(httpClient), TimesheetRestClient {

	private var baseUriV4: URI = UriBuilder.fromUri(serverUri).path("/rest/tempo-timesheets/4").build()
	private var baseUriV3: URI = UriBuilder.fromUri(serverUri).path("/rest/tempo-timesheets/3").build()
	private var baseUriCoreV1: URI = UriBuilder.fromUri(serverUri).path("/rest/tempo-core/1").build()

	override fun createWorkLog(workLog: WorkLogDto): Promise<Void> {
		return this.post(
			UriBuilder.fromUri(this.baseUriV4).path("worklogs").build(),
			workLog,
			WorkLogDtoJsonGenerator(),
		)
	}

	override fun worklogs(form: WorklogsForm): Promise<Worklogs> {
		return this.getAndParse(
			UriBuilder.fromUri(this.baseUriV3)
				.path("worklogs")
				.queryParam("username", form.username)
				.queryParam("dateFrom", form.dateFrom)
				.queryParam("dateTo", form.dateTo)
				.build(),
			WorkLogsDtoJsonParser()
		)
	}

	override fun userSchedule(form: WorklogsForm): Promise<SchedulePeriods> {
		return this.getAndParse(
			UriBuilder.fromUri(this.baseUriCoreV1)
				.path("user/schedule")
				.queryParam("user", form.username)
				.queryParam("from", form.dateFrom)
				.queryParam("to", form.dateTo)
				.build(),
			UserScheduleDtoJsonParser()
		)
	}
}