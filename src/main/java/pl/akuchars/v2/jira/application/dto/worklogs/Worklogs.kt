package pl.akuchars.v2.jira.application.dto.worklogs

import pl.akuchars.v1.kernel.HoursMinutes

class Worklogs(
	private val items: List<Worklog>
) {

	fun countTime(): WorklogsHoursMinutes {
		val overtime = HoursMinutes(items.filter { it.workAttributeValues.isNotEmpty() && it.workAttributeValues[0].value == "2" }
			.mapNotNull(Worklog::timeSpentSeconds)
			.map(Int::toLong)
			.sum())
		val time = HoursMinutes(
			items.filter { it.workAttributeValues.isEmpty() || it.workAttributeValues[0].value != "2" }.mapNotNull(Worklog::timeSpentSeconds)
				.map(Int::toLong)
				.sum()
		)
		return WorklogsHoursMinutes(overtime, time)
	}
}