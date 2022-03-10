package pl.akuchars.jira.timesheets.dto.worklogs

import com.google.gson.annotations.SerializedName

data class Worklog internal constructor(

	@SerializedName("timeSpentSeconds") var timeSpentSeconds: Int? = null,
	@SerializedName("dateStarted") var dateStarted: String? = null,
	@SerializedName("dateCreated") var dateCreated: String? = null,
	@SerializedName("dateUpdated") var dateUpdated: String? = null,
	@SerializedName("comment") var comment: String? = null,
	@SerializedName("self") var self: String? = null,
	@SerializedName("id") var id: Int? = null,
	@SerializedName("jiraWorklogId") var jiraWorklogId: Int? = null,
	@SerializedName("author") internal var author: Author? = Author(),
	@SerializedName("issue") internal var issue: Issue? = Issue(),
	@SerializedName("worklogAttributes") internal var worklogAttributes: ArrayList<WorklogAttributes> = arrayListOf(),
	@SerializedName("workAttributeValues") internal var workAttributeValues: ArrayList<WorkAttributeValues> = arrayListOf()

)