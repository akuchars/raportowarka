package pl.akuchars.jira.timesheets.dto.worklogs

import com.google.gson.annotations.SerializedName

data class Days(
	@SerializedName("date") var date: String? = null,
	@SerializedName("requiredSeconds") var requiredSeconds: Int? = null,
	@SerializedName("type") var type: String? = null
)