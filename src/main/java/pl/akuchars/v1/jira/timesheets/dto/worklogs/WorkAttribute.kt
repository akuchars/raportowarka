package pl.akuchars.v1.jira.timesheets.dto.worklogs

import com.google.gson.annotations.SerializedName

internal data class WorkAttribute(

	@SerializedName("id") var id: Int? = null,
	@SerializedName("key") var key: String? = null,
	@SerializedName("name") var name: String? = null,
	@SerializedName("type") var type: Type? = Type(),
	@SerializedName("externalUrl") var externalUrl: String? = null,
	@SerializedName("required") var required: Boolean? = null,
	@SerializedName("sequence") var sequence: Int? = null

)