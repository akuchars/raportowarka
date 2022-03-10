package pl.akuchars.jira.timesheets.dto.worklogs

import com.google.gson.annotations.SerializedName

internal data class IssueType(

	@SerializedName("name") var name: String? = null,
	@SerializedName("iconUrl") var iconUrl: String? = null

)