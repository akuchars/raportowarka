package akuchars.jtoggl

import akuchars.kernel.properties
import com.natpryce.konfig.Key
import com.natpryce.konfig.stringType

class TooglCredential {
	val apiToken: String = properties[tooglCredentialApiToken]

	companion object {
		private val tooglCredentialApiToken  = Key("toogl.credential.api.token", stringType)
	}
}
