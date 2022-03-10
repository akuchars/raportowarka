package pl.akuchars.koin

import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.module

@KoinApiExtension
class HelloApplication() : KoinComponent {

	// Inject HelloService
	val helloService by inject<HelloService>()

	// display our data
	fun sayHello() = println(helloService.hello())
}

val helloModule = module {

	single { HelloMessageData() }
	single { AnotherRepository() }

	single { HelloServiceImpl(get(), get()) as HelloService }
}

@KoinApiExtension
fun main(vararg args: String) {
	startKoin {
		// use Koin logger
		printLogger()
		// declare modules
		modules(helloModule)
	}

	HelloApplication().sayHello()

}