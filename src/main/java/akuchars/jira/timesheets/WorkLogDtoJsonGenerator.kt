package akuchars.jira.timesheets

import akuchars.jira.timesheets.dto.WorkLogDto
import com.atlassian.jira.rest.client.internal.json.gen.JsonGenerator
import com.google.gson.Gson
import org.codehaus.jettison.json.JSONObject

class WorkLogDtoJsonGenerator : JsonGenerator<WorkLogDto> {

	override fun generate(dto: WorkLogDto): JSONObject {
		return JSONObject(Gson().toJson(dto))
	}
}