package pl.akuchars.v1.jira.timesheets.dto.worklogs

import com.google.gson.annotations.SerializedName

internal data class WorkAttributeValues(

	@SerializedName("id") var id: Int? = null,
	@SerializedName("worklogId") var worklogId: Int? = null,
	@SerializedName("workAttribute") var workAttribute: WorkAttribute? = WorkAttribute(),
	@SerializedName("value") var value: String? = null

)