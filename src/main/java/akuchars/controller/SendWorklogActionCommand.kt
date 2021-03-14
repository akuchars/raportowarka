package akuchars.controller

import java.time.LocalDate

interface SendWorklogActionCommand {
	fun startDate(): LocalDate
	fun endDate(): LocalDate
	fun production(): Boolean
	fun honnest(): Boolean
}