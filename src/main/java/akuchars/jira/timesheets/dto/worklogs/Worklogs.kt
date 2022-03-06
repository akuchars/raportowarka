package akuchars.jira.timesheets.dto.worklogs

import akuchars.kernel.HoursMinutes

class Worklogs(
	private val items: List<Worklog>
) {

	fun countTime(): WorklogsHoursMinutes {
		val overtime = HoursMinutes(items.filter { it.workAttributeValues.isNotEmpty() }
			.mapNotNull(Worklog::timeSpentSeconds)
			.map(Int::toLong)
			.sum())
		val time = HoursMinutes(
			items.filter { it.workAttributeValues.isEmpty() }.mapNotNull(Worklog::timeSpentSeconds)
				.map(Int::toLong)
				.sum()
		)
		return WorklogsHoursMinutes(overtime, time)
	}
}