package pl.akuchars.v2.jtoggl.domain

import org.springframework.stereotype.Component
import pl.akuchars.v1.kernel.HoursMinutes
import pl.akuchars.v2.jtoggl.application.dto.ProjectName
import pl.akuchars.v2.jtoggl.application.dto.TimeEntry
import pl.akuchars.v2.jtoggl.application.dto.TimeEntryDescriptions
import pl.akuchars.v2.jtoggl.application.dto.TimeEntryInformation
import java.time.LocalDate

@Component
class DayTimeEntryFactory {

    // TODO TimeEntry - zamienić na commanda
    fun createDayTime(day: LocalDate, entries: List<TimeEntry>): DayTimeEntry {
        val durations = entries.mapNotNull(TimeEntry::duration).sum().toDouble()

        // TODO HoursMinutes zamienić na Duration
        var hoursMinutes = HoursMinutes(durations.toLong())
        val groupedInformation = entries
            .groupBy(TimeEntry::key)
            .filterKeys { it is String }
            .mapKeys { it.toString() }
            .map { (key: String, e: List<TimeEntry>) -> createTimeEntryInformation(key, e) }


        groupedInformation.forEach { timeEntryInformation ->
            if (timeEntryInformation.isOvertimeEntry || timeEntryInformation.isWorkTimeEntry) {
                hoursMinutes -= timeEntryInformation.hoursMinutes
            }
        }

        return DayTimeEntry(
            hoursMinutes = hoursMinutes,
            day = day,
            workingEntries = groupedInformation.filter(TimeEntryInformation::isOvertimeEntry),
            privateEntries = groupedInformation.filter(TimeEntryInformation::isWorkTimeEntry),
            overtimeEntries = groupedInformation
                .filterNot(TimeEntryInformation::isOvertimeEntry)
                .filterNot(TimeEntryInformation::isWorkTimeEntry),
            workingEntriesWithNoKey = listOf(),
            isWorkHoursDone = durations / 3600 >= 8.0
        )
    }

    private fun createTimeEntryInformation(key: String, entry: List<TimeEntry>): TimeEntryInformation {
        val durations = entry.sumOf { it.duration!! }
        val hourMinutesForEntries = HoursMinutes(durations)

        return TimeEntryInformation(
            key,
            TimeEntryDescriptions(entry.map(TimeEntry::descriptionWithoutKey).toSet()),
            hourMinutesForEntries,
            ProjectName.resolveByCode(entry[0].projectName)
        )
    }
}