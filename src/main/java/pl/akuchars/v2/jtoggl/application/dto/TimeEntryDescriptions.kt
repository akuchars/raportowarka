package pl.akuchars.v2.jtoggl.application.dto

class TimeEntryDescriptions(private val descriptions: Set<String?>) {
	fun values() = descriptions.joinToString(separator = "\n")
}