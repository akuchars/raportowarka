package pl.akuchars.jira.timesheets.dto.worklogs

import java.time.LocalDate

class WorklogsForm(
	val username: String,
	val dateFrom: LocalDate,
	val dateTo: LocalDate
)
