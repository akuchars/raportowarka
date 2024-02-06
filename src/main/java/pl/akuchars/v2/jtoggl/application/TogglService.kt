package pl.akuchars.v2.jtoggl.application

import org.springframework.stereotype.Service
import pl.akuchars.v2.jtoggl.application.dto.TimeEntry
import pl.akuchars.v2.jtoggl.domain.DayTimeEntry
import pl.akuchars.v2.jtoggl.domain.DayTimeEntryFactory
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@Service
class TogglService(
    private val queryService: TogglQueryService,
    private val factory: DayTimeEntryFactory
) {

    fun timeEntriesByDay(start: LocalDate, end: LocalDate): List<DayTimeEntry> {
        val startDate = start.toDate()
        val endDate = end.toDate()
        return queryService.getTimeEntries(startDate, endDate)
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