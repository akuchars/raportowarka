package akuchars.jtoggl.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.math.abs

object DateUtil {
	private const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

	fun convertStringToDate(dateString: String?): Date? {
		if (dateString == null) return null
		val sdf = SimpleDateFormat(DATE_FORMAT)
		var date: Date? = null
		try {
			date = sdf.parse(dateString)
		} catch (ex: ParseException) {
			Logger.getLogger(DateUtil::class.java.name).log(Level.SEVERE, null, ex)
		}
		return date
	}

	fun convertDateToString(date: Date): String {
		val sdf = SimpleDateFormat(DATE_FORMAT)
		val timezoneOffset = date.timezoneOffset
		val hour = abs(timezoneOffset / 60)
		val min = abs(timezoneOffset % 60)
		val dateTime = sdf.format(date)
		val timeOffset = (if (timezoneOffset <= 0) "+" else "-") + (if (hour < 10) "0" else "") + hour + ":" + (if (min < 10) "0" else "") + min
		return dateTime + timeOffset
	}
}