package pl.akuchars.v1.controller

import java.time.LocalDate

interface SendWorklogActionCommand {
	fun startDate(): LocalDate
	fun endDate(): LocalDate
	fun production(): Boolean
	fun honnest(): Boolean
}