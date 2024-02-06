package pl.akuchars.v2.jira.infrastructure

import pl.akuchars.v2.jira.application.TimesheetRestClient
import com.atlassian.jira.rest.client.api.JiraRestClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.akuchars.v2.jira.domain.JiraCredential

private lateinit var credential: JiraCredential
private lateinit var jiraRestClient: JiraRestClientWithExtraClients

@Configuration
@EnableConfigurationProperties(value = [JiraCredential::class])
class JiraClientConfiguration {

    @Bean
    fun createWithExtraClients(jiraCredential: JiraCredential): JiraRestClient {
        credential = jiraCredential
        jiraRestClient = JiraRestClientWithExtraClients(credential)
        return jiraRestClient.jiraRestClient
    }
}

internal val JiraRestClient.timesheetClient: TimesheetRestClient
    get() = jiraRestClient.timesheetClient

