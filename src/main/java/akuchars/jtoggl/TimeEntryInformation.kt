package akuchars.jtoggl

import akuchars.kernel.HoursMinutes

class TimeEntryInformation(val key: String,
						   val description: TimeEntryDescriptions,
						   var hoursMinutes: HoursMinutes,
						   private val projectName: ProjectName?
) {
	fun fixHour(howManyToAdd: HoursMinutes) {
		hoursMinutes += howManyToAdd
	}

	val isWorkTimeEntry: Boolean
		get() = projectName?.isWorkProject ?: false

}