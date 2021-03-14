package akuchars.kernel

import com.natpryce.konfig.ConfigurationProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

val allLoggersMap = ConcurrentHashMap<KClass<*>, Logger>()

internal val propertiesLazy = lazy {
	ConfigurationProperties.fromResource("defaults.properties")
}

inline val <reified T : Any> KClass<T>.logger: Logger
	get() = allLoggersMap.getOrPut(T::class, { LoggerFactory.getLogger(T::class.java) })

inline val <reified T : Any> T.logger: Logger
	get() = allLoggersMap.getOrPut(T::class, { LoggerFactory.getLogger(T::class.java) })

internal inline val <reified T : Any> KClass<T>.properties: ConfigurationProperties
	get() = propertiesLazy.value

internal inline val <reified T : Any> T.properties: ConfigurationProperties
	get() = propertiesLazy.value