package pl.akuchars.v2.jtoggl.application.dto

import pl.akuchars.v1.kernel.HoursMinutes

class TimeEntryInformation(
    val key: String,
    val description: TimeEntryDescriptions,
    var hoursMinutes: HoursMinutes,
    private val projectName: ProjectName?
) {
	fun fixHour(howManyToAdd: HoursMinutes) {
		hoursMinutes += howManyToAdd
	}

	val isWorkTimeEntry: Boolean
		get() = projectName?.isWorkProject ?: false

	val isOvertimeEntry: Boolean
		get() = projectName?.isOvertimeProject ?: false

}