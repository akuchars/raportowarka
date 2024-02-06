package pl.akuchars.v2.jira.application.dto

import com.google.gson.annotations.SerializedName
import pl.akuchars.v2.jira.application.dto.Attributes
import pl.akuchars.v2.jira.application.dto.Issue
import pl.akuchars.v2.jira.application.dto.Location


data class WorkLogResultDto(

    @SerializedName("billableSeconds") val billableSeconds: Int,
    @SerializedName("timeSpentSeconds") val timeSpentSeconds: Int,
    @SerializedName("tempoWorklogId") val tempoWorklogId: Int,
    @SerializedName("issue") val issue: Issue,
    @SerializedName("comment") val comment: String,
    @SerializedName("location") val location: Location,
    @SerializedName("attributes") val attributes: Attributes,
    @SerializedName("started") val started: String,
    @SerializedName("dateCreated") val dateCreated: String,
    @SerializedName("dateUpdated") val dateUpdated: String,
    @SerializedName("originTaskId") val originTaskId: Int,
    @SerializedName("updater") val updater: String,
    @SerializedName("worker") val worker: String,
    @SerializedName("originId") val originId: Int
)