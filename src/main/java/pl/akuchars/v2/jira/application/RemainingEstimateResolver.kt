package pl.akuchars.v2.jira.application

import com.atlassian.jira.rest.client.api.JiraRestClient
import org.springframework.stereotype.Component
import pl.akuchars.v2.jtoggl.application.dto.TimeEntryInformation
import kotlin.math.max

@Component
internal class RemainingEstimateResolver(private val jiraClient: JiraRestClient) : (TimeEntryInformation) -> Int {
    override fun invoke(timeEntry: TimeEntryInformation): Int {
        val currentRemaining = (jiraClient.issueClient.getIssue(timeEntry.key)
            .fail { System.err.println("Cannot find issue by key: ${timeEntry.key}") }
            .claim()
            .timeTracking?.remainingEstimateMinutes ?: 0) * 60
        return max((currentRemaining - timeEntry.hoursMinutes.duration / 60), 0).toInt()
    }
}