package pl.akuchars.v1.model

import java.time.LocalDate

class WorklogsForm(
	val username: String,
	val dateFrom: LocalDate,
	val dateTo: LocalDate
)
