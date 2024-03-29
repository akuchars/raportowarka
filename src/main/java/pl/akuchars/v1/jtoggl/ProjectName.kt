package pl.akuchars.v1.jtoggl

import pl.akuchars.v1.kernel.properties
import com.natpryce.konfig.Key
import com.natpryce.konfig.listType
import com.natpryce.konfig.stringType

class ProjectName(private val code: String, val isWorkProject: Boolean, val isOvertimeProject: Boolean = false) {
	companion object {
		fun resolveByCode(code: String?): ProjectName? {
			return Projects.values
				.map { ProjectName(it, true, it.equals("NADGODZINY", ignoreCase = true)) }
				.find { it.code == code }
		}
	}
}

object Projects {
	private val projectKeySeparator = Key("toggl.working.projects.list.separator", stringType)
	private val projectsSeparator: String = properties[projectKeySeparator]
	private val projectKey = Key("toggl.working.projects.list", listType(stringType, projectsSeparator.toRegex()))
	val values: List<String> = properties[projectKey]
}