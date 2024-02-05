package pl.akuchars.v2.jtoggl.application

import pl.akuchars.v2.jtoggl.domain.DayTimeEntry
import pl.akuchars.v2.jtoggl.application.dto.Project
import pl.akuchars.v2.jtoggl.application.dto.TimeEntry
import pl.akuchars.v2.jtoggl.application.dto.Workspace
import java.time.LocalDate
import java.util.*

interface TogglQueryService {
    fun getTimeEntries(startDate: Date?, endDate: Date?): List<TimeEntry>
    fun timeEntriesByDay(start: LocalDate, end: LocalDate): List<DayTimeEntry>
    fun workspaces(): List<Workspace>
    fun projects(): List<Project>
    fun getWorkspaceProjects(workspaceId: Long): List<Project>
}