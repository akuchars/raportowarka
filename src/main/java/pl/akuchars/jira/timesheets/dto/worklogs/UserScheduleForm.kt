package pl.akuchars.jira.timesheets.dto.worklogs

import java.time.LocalDate

class UserScheduleForm(
	val user: String,
	val dateFrom: LocalDate,
	val dateTo: LocalDate
)
