package pl.akuchars.v1.jira

import com.atlassian.jira.rest.client.api.JiraRestClient
import pl.akuchars.v1.jira.timesheets.TimesheetRestClient


private lateinit var credential: JiraCredential
private lateinit var jiraRestClient: JiraRestClientWithExtraClients

fun createWithExtraClients(jiraCredential: JiraCredential): JiraRestClient {
	credential = jiraCredential
	jiraRestClient = JiraRestClientWithExtraClients(credential)
	return jiraRestClient.jiraRestClient
}

val JiraRestClient.timesheetClient: TimesheetRestClient
	get() = jiraRestClient.timesheetRestClient


