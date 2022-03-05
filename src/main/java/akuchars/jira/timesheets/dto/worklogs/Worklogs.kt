package akuchars.jira.timesheets.dto.worklogs

import akuchars.kernel.HoursMinutes

class Worklogs(
	private val items: List<Worklog>
) {

	fun toOvertime(): Worklogs {
		return Worklogs(items.filter { it.workAttributeValues.isNotEmpty() })
	}

	fun countTime(): HoursMinutes {
		return HoursMinutes(items.mapNotNull(Worklog::timeSpentSeconds)
			.map(Int::toLong)
			.sum())
	}
}