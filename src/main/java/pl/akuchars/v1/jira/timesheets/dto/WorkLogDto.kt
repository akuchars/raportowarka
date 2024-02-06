package pl.akuchars.v1.jira.timesheets.dto

import pl.akuchars.v1.jira.timesheets.dto.OvertimeAttributeDto

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