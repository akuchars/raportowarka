package akuchars.jtoggl

import java.time.LocalDate
import java.util.*

class DayTimeEntry(val day: LocalDate?,
				   entries: List<TimeEntry>
) {
	private var hoursMinutes: HoursMinutes
	val workingEntries: MutableList<TimeEntryInformation> = ArrayList()
	private val privateEntries: MutableList<TimeEntryInformation> = ArrayList()
	val workingEntriesWithNoKey: MutableList<TimeEntryInformation> = ArrayList()
	private val isWorkHoursDone: Boolean

	private fun addToDedicatedList(timeEntryInformation: TimeEntryInformation) {
		if (timeEntryInformation.isWorkTimeEntry) {
			workingEntries += timeEntryInformation
		} else {
			hoursMinutes -= timeEntryInformation.hoursMinutes
			privateEntries += timeEntryInformation
		}
	}

	private fun createTimeEntryInformation(key: String, e: List<TimeEntry>): TimeEntryInformation {
		return TimeEntryInformation(
			key,
			getDescriptionsForCurrentKeys(e),
			getHoursMinutesForEntries(e),
			ProjectName.resolveByCode(e[0].projectName))
	}

	private fun getDescriptionsForCurrentKeys(entries: List<TimeEntry>): TimeEntryDescriptions {
		return TimeEntryDescriptions(entries.map { it.descriptionWithoutKey }.toSet())
	}

	private fun getHoursMinutesForEntries(entries: List<TimeEntry>): HoursMinutes {
		val durations = entries.map { it.duration!! }.reduce { acc, l -> acc + l }.toDouble()
		return HoursMinutes(durations.toLong())
	}

	fun fixToWorkHour() {
		if (!isWorkHoursDone && workingEntries.isNotEmpty()) {
			val howManyToAdd: HoursMinutes = (HoursMinutes.workHours() - hoursMinutes)
				.countAverage(workingEntries.size)
			workingEntries.forEach { it.fixHour(howManyToAdd) }
		}
	}

	init {
		val durations = entries.map { it.duration!! }.reduce { acc, l -> acc + l }.toDouble()
		hoursMinutes = HoursMinutes(durations.toLong())
		isWorkHoursDone = durations / 3600 >= 8.0
		entries
			.groupBy { it.key }
			.forEach { (key: String?, e: List<TimeEntry>) ->
				if (key is String) {
					val timeEntryInformation = createTimeEntryInformation(key, e)
					addToDedicatedList(timeEntryInformation)
				} else {
					println("There is problem to generate key: ${e[0].description}")
					workingEntriesWithNoKey + e
				}
			}
	}
}