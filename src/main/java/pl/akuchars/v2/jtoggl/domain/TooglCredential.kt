package pl.akuchars.v2.jtoggl.domain

import pl.akuchars.v1.kernel.properties
import com.natpryce.konfig.Key
import com.natpryce.konfig.stringType

class TooglCredential {
	val apiToken: String = properties[tooglCredentialApiToken]

	companion object {
		private val tooglCredentialApiToken  = Key("toogl.credential.api.token", stringType)
	}
}
