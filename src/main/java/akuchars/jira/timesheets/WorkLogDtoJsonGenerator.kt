package akuchars.jira.timesheets

import akuchars.jira.timesheets.dto.WorkLogDto
import akuchars.jira.timesheets.dto.worklogs.SchedulePeriods
import akuchars.jira.timesheets.dto.worklogs.Worklog
import akuchars.jira.timesheets.dto.worklogs.Worklogs
import com.atlassian.jira.rest.client.internal.json.JsonArrayParser
import com.atlassian.jira.rest.client.internal.json.JsonObjectParser
import com.atlassian.jira.rest.client.internal.json.gen.JsonGenerator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.codehaus.jettison.json.JSONArray
import org.codehaus.jettison.json.JSONObject


class WorkLogDtoJsonGenerator : JsonGenerator<WorkLogDto> {

	override fun generate(dto: WorkLogDto): JSONObject {
		return JSONObject(Gson().toJson(dto))
	}
}

class WorkLogsDtoJsonParser : JsonArrayParser<Worklogs> {
	override fun parse(resultArray: JSONArray): Worklogs {
		val type = object : TypeToken<List<Worklog>>() {}.type
		return Worklogs(Gson().fromJson(resultArray.toString(), type))
	}
}

class UserScheduleDtoJsonParser: JsonObjectParser<SchedulePeriods> {

	override fun parse(result: JSONObject): SchedulePeriods {
		return Gson().fromJson(result.toString(), SchedulePeriods::class.java)
	}
}