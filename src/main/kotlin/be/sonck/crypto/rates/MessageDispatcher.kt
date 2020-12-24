package be.sonck.crypto.rates

import be.sonck.crypto.rates.adapter.environment.EnvironmentAdapter
import be.sonck.crypto.rates.adapter.ses.SesAdapter
import be.sonck.crypto.rates.adapter.sns.SnsAdapter
import java.util.function.Consumer

class MessageDispatcher(
        private val environmentAdapter: EnvironmentAdapter = EnvironmentAdapter(),
        private val consumers: Map<String, Consumer<String>> = mapOf(
                "SNS" to SnsAdapter(),
                "SES" to SesAdapter()
        )
): Consumer<String> {
    override fun accept(message: String) {
        val consumer = environmentAdapter.getValue("messageConsumer", "SES")
        (consumers[consumer] ?: throw IllegalStateException("no consumers found for $consumer"))
                .accept(message)
    }
}