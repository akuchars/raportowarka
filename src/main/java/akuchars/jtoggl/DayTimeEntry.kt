package akuchars.jtoggl

import akuchars.kernel.HoursMinutes
import akuchars.kernel.properties
import com.natpryce.konfig.Key
import com.natpryce.konfig.listType
import com.natpryce.konfig.stringType
import java.time.LocalDate
import java.util.*

class DayTimeEntry(val day: LocalDate?,
				   entries: List<TimeEntry>
) {
	private var hoursMinutes: HoursMinutes
	val workingEntries: MutableList<TimeEntryInformation> = ArrayList()
	private val privateEntries: MutableList<TimeEntryInformation> = ArrayList()
	private val overtimeEntries: MutableList<TimeEntryInformation> = ArrayList()
	val workingEntriesWithNoKey: MutableList<TimeEntryInformation> = ArrayList()
	private val isWorkHoursDone: Boolean

	private fun addToDedicatedList(timeEntryInformation: TimeEntryInformation) {
		if (timeEntryInformation.isOvertimeEntry) {
			overtimeEntries += timeEntryInformation
			hoursMinutes -= timeEntryInformation.hoursMinutes
		}
		if (timeEntryInformation.isWorkTimeEntry) {
			workingEntries += timeEntryInformation
		} else {
			hoursMinutes -= timeEntryInformation.hoursMinutes
			privateEntries += timeEntryInformation
		}
	}

	private fun createTimeEntryInformation(key: String, entry: List<TimeEntry>): TimeEntryInformation {
		return TimeEntryInformation(
			key,
			getDescriptionsForCurrentKeys(entry),
			getHoursMinutesForEntries(entry),
			ProjectName.resolveByCode(entry[0].projectName))
	}

	private fun getDescriptionsForCurrentKeys(entries: List<TimeEntry>): TimeEntryDescriptions {
		return TimeEntryDescriptions(entries.map { it.descriptionWithoutKey }.toSet())
	}

	private fun getHoursMinutesForEntries(entries: List<TimeEntry>): HoursMinutes {
		val durations = entries.map { it.duration!! }.reduce { acc, l -> acc + l }.toDouble()
		return HoursMinutes(durations.toLong())
	}

	fun fixToWorkHour() {
		val notFixingIssuesList: List<String> = properties[Key("issue.to.not.fixing.hours", listType(stringType, ";".toRegex()))]
		if (!isWorkHoursDone && workingEntries.isNotEmpty()) {
			val workEntriesToFix = workingEntries
				.filterNot { notFixingIssuesList.contains(it.key) }
				.filterNot(overtimeEntries::contains);
			if (workEntriesToFix.isNotEmpty()) {
				val howManyToAdd: HoursMinutes = (HoursMinutes.workHours() - hoursMinutes)
					.countAverage(workEntriesToFix.size)
				workEntriesToFix
					.forEach { it.fixHour(howManyToAdd) }
			}
		}
	}

	init {
		val durations = entries.mapNotNull(TimeEntry::duration).sum().toDouble()
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