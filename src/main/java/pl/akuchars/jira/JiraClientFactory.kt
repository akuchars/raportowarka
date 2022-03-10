package pl.akuchars.jira

import pl.akuchars.jira.timesheets.TimesheetRestClient
import com.atlassian.jira.rest.client.api.JiraRestClient


private lateinit var credential: JiraCredential
private lateinit var jiraRestClient: JiraRestClientWithExtraClients

fun createWithExtraClients(jiraCredential: JiraCredential): JiraRestClient {
	credential = jiraCredential
	jiraRestClient = JiraRestClientWithExtraClients(credential)
	return jiraRestClient.jiraRestClient
}

val JiraRestClient.timesheetClient: TimesheetRestClient
	get() = jiraRestClient.timesheetRestClient


