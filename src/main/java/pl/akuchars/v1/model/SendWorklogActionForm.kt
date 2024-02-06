package pl.akuchars.v1.model

import pl.akuchars.v1.controller.SendWorklogActionCommand
import java.time.LocalDate

class SendWorklogActionForm(
	private val start: LocalDate,
	private val end: LocalDate,
	private val production: Boolean = true,
	private val honest: Boolean = false
) : SendWorklogActionCommand {
	override fun startDate(): LocalDate = start
	override fun endDate(): LocalDate = end
	override fun production(): Boolean = production
	override fun honest(): Boolean = honest
}