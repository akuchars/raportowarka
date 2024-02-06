package pl.akuchars.v1.jira.timesheets.dto.worklogs

import com.google.gson.annotations.SerializedName

internal data class Issue(

    @SerializedName("self") var self: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("projectId") var projectId: Int? = null,
    @SerializedName("key") var key: String? = null,
    @SerializedName("remainingEstimateSeconds") var remainingEstimateSeconds: Int? = null,
    @SerializedName("issueType") var issueType: IssueType? = IssueType(),
    @SerializedName("summary") var summary: String? = null

)