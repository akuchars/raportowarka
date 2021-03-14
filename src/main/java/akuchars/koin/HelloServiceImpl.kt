package akuchars.koin

class HelloServiceImpl(private val helloMessageData: HelloMessageData, private val repository : AnotherRepository) : HelloService {

	override fun hello() = "Hey, ${helloMessageData.message} + ${repository::class.simpleName}"
}