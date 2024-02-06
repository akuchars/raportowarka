package pl.akuchars.v2.jira.application.dto

import pl.akuchars.v1.jira.timesheets.dto.AttributesDto

class OvertimeAttributeDto(

	val _Overtime_: AttributesDto = AttributesDto(
		"Overtime", "2", 1L
	)
)

class AttributesDto(
	val key: String,
	val value: String,
	val workAttributeId: Long
)
