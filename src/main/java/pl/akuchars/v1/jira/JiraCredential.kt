package pl.akuchars.v1.jira

import com.natpryce.konfig.Key
import com.natpryce.konfig.stringType
import pl.akuchars.v1.kernel.envStringType
import pl.akuchars.v1.kernel.extraGet
import pl.akuchars.v1.kernel.properties

class JiraCredential {
	val url: String = properties.extraGet(jiraUrl)
	val user: String = properties.extraGet(userUrl)
	val password: String = properties.extraGet(passwordUrl)

	companion object {
		private val jiraUrl = Key("jira.credential.url", stringType)
		private val userUrl = Key("jira.credential.user", stringType)
		private val passwordUrl = Key("jira.credential.password", envStringType)
	}
}