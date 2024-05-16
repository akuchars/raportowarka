package pl.akuchars.v1.jtoggl

import pl.akuchars.v1.kernel.properties
import com.natpryce.konfig.Key
import pl.akuchars.v1.kernel.envStringType
import pl.akuchars.v1.kernel.extraGet

class TogglCredential {
	val apiToken: String = properties.extraGet(togglCredentialApiToken)

	companion object {
		private val togglCredentialApiToken  = Key("toggl.credential.api-token", envStringType)
	}
}
