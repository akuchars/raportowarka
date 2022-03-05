package akuchars.jira.timesheets.dto.worklogs

import com.google.gson.annotations.SerializedName

internal data class Type(

	@SerializedName("name") var name: String? = null,
	@SerializedName("value") var value: String? = null,
	@SerializedName("systemType") var systemType: Boolean? = null

)