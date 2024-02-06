package pl.akuchars.v2.jtoggl.domain

import com.natpryce.konfig.Key
import com.natpryce.konfig.listType
import com.natpryce.konfig.stringType
import pl.akuchars.v1.kernel.HoursMinutes
import pl.akuchars.v1.kernel.properties
import pl.akuchars.v2.jtoggl.application.dto.TimeEntryInformation
import java.time.LocalDate

class DayTimeEntry(
    private var hoursMinutes: HoursMinutes,
    val workingEntries: List<TimeEntryInformation>,
    val day: LocalDate,
    private val privateEntries: List<TimeEntryInformation>,
    private val overtimeEntries: List<TimeEntryInformation>,
    internal val workingEntriesWithNoKey: List<TimeEntryInformation>,
    private val isWorkHoursDone: Boolean
) {

    fun fixToWorkHour() {
        // TODO Zmienią się propertisy Springowe
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
}