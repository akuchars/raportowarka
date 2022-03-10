package pl.akuchars.jira

import pl.akuchars.jira.timesheets.AsynchronousTimesheetRestClient
import pl.akuchars.jira.timesheets.TimesheetRestClient
import com.atlassian.httpclient.api.HttpClient
import com.atlassian.jira.rest.client.api.AuthenticationHandler
import com.atlassian.jira.rest.client.api.JiraRestClient
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler
import com.atlassian.jira.rest.client.internal.async.AsynchronousHttpClientFactory
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory
import java.net.URI

class JiraRestClientWithExtraClients(jiraCredential: JiraCredential) {

	var jiraRestClient: JiraRestClient
	var timesheetRestClient: TimesheetRestClient

	init {
		val authenticationHandler: AuthenticationHandler = BasicHttpAuthenticationHandler(jiraCredential.user, jiraCredential.password)
		val uri = URI.create(jiraCredential.url)
		val httpClient: HttpClient = AsynchronousHttpClientFactory().createClient(uri, authenticationHandler)
		timesheetRestClient = AsynchronousTimesheetRestClient(httpClient, uri)
		jiraRestClient = AsynchronousJiraRestClientFactory()
			.create(URI.create(jiraCredential.url), httpClient)
	}
}
