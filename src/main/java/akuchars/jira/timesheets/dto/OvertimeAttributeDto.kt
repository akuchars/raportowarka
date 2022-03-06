package akuchars.jira.timesheets.dto

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
