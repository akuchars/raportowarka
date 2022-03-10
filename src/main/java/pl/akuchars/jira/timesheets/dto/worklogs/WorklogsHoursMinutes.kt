package pl.akuchars.jira.timesheets.dto.worklogs

import pl.akuchars.kernel.HoursMinutes

class WorklogsHoursMinutes(
	val overtime: HoursMinutes,
	val time: HoursMinutes
) {
	lateinit var requiredHours : HoursMinutes
}