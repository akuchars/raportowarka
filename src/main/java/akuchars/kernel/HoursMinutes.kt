package akuchars.kernel

import kotlin.math.ceil


class HoursMinutes {
	val duration: Long
		get() = hours * 3600 + minutes * 60

	private val hours: Long
	private val minutes: Long

	constructor(duration: Long) {
		hours = duration / 3600
		minutes = (duration - hours * 3600) / 60
	}

	private constructor(hours: Long, minutes: Long) {
		this.hours = hours
		this.minutes = minutes
	}

	fun countAverage(size: Int): HoursMinutes {
		val duration = hours * 3600 + minutes * 60
		return HoursMinutes(ceil(duration.toDouble() / size.toDouble()).toLong())
	}

	operator fun minus(h2: HoursMinutes): HoursMinutes {
		val h1Duration = hours * 3600 + minutes * 60
		val h2Duration = h2.hours * 3600 + h2.minutes * 60
		return HoursMinutes(h1Duration - h2Duration)
	}

	operator fun plus(h2: HoursMinutes): HoursMinutes {
		val h1Duration = hours * 3600 + minutes * 60
		val h2Duration = h2.hours * 3600 + h2.minutes * 60
		return HoursMinutes(h1Duration + h2Duration)
	}

	override fun toString(): String {
		return "$hours H $minutes M"
	}

	companion object {
		fun workHours(): HoursMinutes {
			return HoursMinutes(8, 0)
		}
	}
}