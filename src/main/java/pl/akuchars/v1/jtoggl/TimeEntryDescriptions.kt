package pl.akuchars.v1.jtoggl

class TimeEntryDescriptions(private val descriptions: Set<String?>) {
	fun values() = descriptions.joinToString(separator = "\n")
}