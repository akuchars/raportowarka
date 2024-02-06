package pl.akuchars.v2.jira.application.dto.worklogs

import com.google.gson.annotations.SerializedName

internal data class IssueType(

	@SerializedName("name") var name: String? = null,
	@SerializedName("iconUrl") var iconUrl: String? = null

)