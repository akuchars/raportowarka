package pl.akuchars.v1.kernel

import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.Key
import com.natpryce.konfig.ParseResult
import com.natpryce.konfig.propertyType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

val allLoggersMap = ConcurrentHashMap<KClass<*>, Logger>()

internal val propertiesLazy = lazy {
    ConfigurationProperties.fromResource("defaults.properties")
}

inline val <reified T : Any> KClass<T>.logger: Logger
    get() = allLoggersMap.getOrPut(T::class) { LoggerFactory.getLogger(T::class.java) }

inline val <reified T : Any> T.logger: Logger
    get() = allLoggersMap.getOrPut(T::class) { LoggerFactory.getLogger(T::class.java) }

internal inline val <reified T : Any> KClass<T>.properties: ConfigurationProperties
    get() = propertiesLazy.value

internal inline val <reified T : Any> T.properties: ConfigurationProperties
    get() = propertiesLazy.value

fun <T> ConfigurationProperties.extraGet(key: Key<T>): T {
    return when (key.parse) {
        envStringType -> {
            val envKey = (this[key] as String).replace("ENV[", "").replace("]", "")
            return System.getenv(envKey) as T
        }

        else -> this[key]
    }

}

val envStringType = propertyType { ParseResult.Success(it) }