package pl.akuchars.jira.timesheets.dto.worklogs

import com.google.gson.annotations.SerializedName

internal data class WorklogAttributes(

	@SerializedName("key") var key: String? = null,
	@SerializedName("value") var value: String? = null

)