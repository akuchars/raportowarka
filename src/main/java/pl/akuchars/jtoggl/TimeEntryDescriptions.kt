package pl.akuchars.jtoggl

class TimeEntryDescriptions(private val descriptions: Set<String?>) {
	fun values() = descriptions.joinToString(separator = "\n")
}