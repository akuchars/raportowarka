package pl.akuchars.v2.jira.infrastructure

import pl.akuchars.v2.jira.application.TimesheetRestClient
import com.atlassian.httpclient.api.HttpClient
import com.atlassian.jira.rest.client.api.AuthenticationHandler
import com.atlassian.jira.rest.client.api.JiraRestClient
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler
import com.atlassian.jira.rest.client.internal.async.AsynchronousHttpClientFactory
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory
import pl.akuchars.v2.jira.domain.JiraCredential
import java.net.URI

class JiraRestClientWithExtraClients(jiraCredential: JiraCredential) {

	var jiraRestClient: JiraRestClient
	var timesheetClient: TimesheetRestClient

	init {
		val authenticationHandler: AuthenticationHandler = BasicHttpAuthenticationHandler(jiraCredential.user, jiraCredential.password)
		val uri = URI.create(jiraCredential.url)
		val httpClient: HttpClient = AsynchronousHttpClientFactory().createClient(uri, authenticationHandler)
		timesheetClient = AsynchronousTimesheetRestClient(httpClient, uri)
		jiraRestClient = AsynchronousJiraRestClientFactory()
			.create(URI.create(jiraCredential.url), httpClient)
	}
}
