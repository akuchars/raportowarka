package pl.akuchars.jira.timesheets.dto.worklogs

import com.google.gson.annotations.SerializedName

class SchedulePeriods {

	@SerializedName("numberOfWorkingDays") var numberOfWorkingDays: Int? = null
	@SerializedName("requiredSeconds") var requiredSeconds: Long? = null
	@SerializedName("days") var days: ArrayList<Days> = arrayListOf()

//	@Transient var requiredHoursMinutes: HoursMinutes = HoursMinutes(requiredSeconds!!)

}