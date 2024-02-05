package pl.akuchars.v2.jtoggl.domain

import com.natpryce.konfig.Key
import com.natpryce.konfig.listType
import com.natpryce.konfig.stringType
import pl.akuchars.v1.kernel.HoursMinutes
import pl.akuchars.v1.kernel.properties
import pl.akuchars.v2.jtoggl.application.dto.TimeEntryInformation

class DayTimeEntry(
    private var hoursMinutes: HoursMinutes,
    val workingEntries: List<TimeEntryInformation> = ArrayList(),
    private val privateEntries: List<TimeEntryInformation> = ArrayList(),
    private val overtimeEntries: List<TimeEntryInformation> = ArrayList(),
    val workingEntriesWithNoKey: List<TimeEntryInformation> = ArrayList(),
    private val isWorkHoursDone: Boolean
) {

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
}