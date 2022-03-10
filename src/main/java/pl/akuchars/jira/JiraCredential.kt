package pl.akuchars.jira

import pl.akuchars.kernel.properties
import com.natpryce.konfig.Key
import com.natpryce.konfig.stringType

class JiraCredential {
	val url: String = properties[jiraUrl]
	val user: String = properties[userUrl]
	val password: String = properties[passwordUrl]

	companion object {
		private val jiraUrl = Key("jira.credential.url", stringType)
		private val userUrl = Key("jira.credential.user", stringType)
		private val passwordUrl = Key("jira.credential.password", stringType)
	}
}