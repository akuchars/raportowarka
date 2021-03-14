package akuchars.jira.timesheets

import akuchars.jira.timesheets.dto.WorkLogDto
import com.atlassian.httpclient.api.HttpClient
import com.atlassian.jira.rest.client.internal.async.AbstractAsynchronousRestClient
import com.atlassian.util.concurrent.Promise
import java.net.URI
import javax.ws.rs.core.UriBuilder

class AsynchronousTimesheetRestClient(httpClient: HttpClient, serverUri: URI)
	: AbstractAsynchronousRestClient(httpClient), TimesheetRestClient {

	private var baseUri: URI = UriBuilder.fromUri(serverUri).path("/rest/tempo-timesheets/4").build()

	override fun createWorkLog(workLog: WorkLogDto): Promise<Void> {
		return this.post(
			UriBuilder.fromUri(this.baseUri).path("worklogs").build(),
			workLog,
			WorkLogDtoJsonGenerator(),
		)
	}
}