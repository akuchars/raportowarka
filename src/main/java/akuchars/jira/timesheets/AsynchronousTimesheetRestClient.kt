package akuchars.jira.timesheets

import akuchars.jira.timesheets.dto.WorkLogDto
import akuchars.jira.timesheets.dto.worklogs.Worklogs
import akuchars.jira.timesheets.dto.worklogs.WorklogsForm
import com.atlassian.httpclient.api.HttpClient
import com.atlassian.jira.rest.client.internal.async.AbstractAsynchronousRestClient
import com.atlassian.util.concurrent.Promise
import java.net.URI
import javax.ws.rs.core.UriBuilder

//https://apidocs.tempo.io/
class AsynchronousTimesheetRestClient(httpClient: HttpClient, serverUri: URI) : AbstractAsynchronousRestClient(httpClient), TimesheetRestClient {

	private var baseUriV4: URI = UriBuilder.fromUri(serverUri).path("/rest/tempo-timesheets/4").build()
	private var baseUriV3: URI = UriBuilder.fromUri(serverUri).path("/rest/tempo-timesheets/3").build()

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
}