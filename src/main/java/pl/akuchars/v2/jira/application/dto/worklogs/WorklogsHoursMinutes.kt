package pl.akuchars.v2.jira.application.dto.worklogs

import pl.akuchars.v1.kernel.HoursMinutes

class WorklogsHoursMinutes(
	val overtime: HoursMinutes,
	val time: HoursMinutes
) {
	lateinit var requiredHours : HoursMinutes
}