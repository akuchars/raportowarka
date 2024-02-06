package pl.akuchars.v2.jtoggl.domain

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "toggl.credential")
class TogglCredential @ConstructorBinding constructor(val apiToken: String)
