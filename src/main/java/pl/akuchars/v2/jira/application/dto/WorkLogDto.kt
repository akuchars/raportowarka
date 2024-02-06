package pl.akuchars.v2.jira.application.dto

class WorkLogDto(
	val worker: String,
	val comment: String,
	val started: String,
	val endDate: String?,
	val timeSpentSeconds: Int,
	val billableSeconds: Int?,
	val originTaskId: String,
	val remainingEstimate: Int,
	val includeNonWorkingDays: Boolean = false,
	val attributes: OvertimeAttributeDto?
)