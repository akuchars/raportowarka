package pl.akuchars.v2.jtoggl.application

import org.springframework.stereotype.Service
import pl.akuchars.v1.controller.SendWorklogActionCommand
import pl.akuchars.v2.jtoggl.application.dto.PeriodTimeEntry
import pl.akuchars.v2.jtoggl.application.dto.TimeEntry
import pl.akuchars.v2.jtoggl.domain.DayTimeEntry
import pl.akuchars.v2.jtoggl.domain.DayTimeEntryFactory
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@Service
class TogglService internal constructor(
    private val queryService: TogglRestClient,
    private val factory: DayTimeEntryFactory
) {

    fun getWorklogsForPeriod(command: SendWorklogActionCommand): PeriodTimeEntry =
        PeriodTimeEntry(getTogglWorkLogs(command))

    private fun getTogglWorkLogs(command: SendWorklogActionCommand): List<DayTimeEntry> {
        return fetchEntriesByDay(command.startDate(), command.endDate()).onEach {
            if (!command.honest()) {
                it.fixToWorkHour()
            }
            if (it.workingEntriesWithNoKey.isNotEmpty())
                println("There is a problem to resolve key for: ${it.day}: ${it.workingEntriesWithNoKey}")
        }
    }

    private fun fetchEntriesByDay(start: LocalDate, end: LocalDate): List<DayTimeEntry> {
        return queryService.getTimeEntries(start.toDate(), end.toDate())
            .groupBy(TimeEntry::day)
            .mapValues { (day, entriesByDay) -> factory.createDayTime(day, entriesByDay) }
            .values.toList()
    }

    private fun LocalDate.toDate(): Date {
        return Date.from(
            this.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant()
        )
    }

}