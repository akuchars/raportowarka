package pl.akuchars.v2.jira.domain

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "jira.credential")
class JiraCredential @ConstructorBinding constructor(
    val url: String,
    val user: String,
    val password: String,
)