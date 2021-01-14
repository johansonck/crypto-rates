package be.sonck.crypto.adapter.sns

import be.sonck.crypto.adapter.environment.EnvironmentAdapter
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sns.model.PublishRequest
import java.util.function.Consumer

class SnsAdapter(
        private val snsClient: SnsClient = SnsClient.builder().region(Region.EU_WEST_1).build(),
        private val environmentAdapter: EnvironmentAdapter = EnvironmentAdapter()
) : Consumer<String> {

    override fun accept(message: String) {
        val request = PublishRequest.builder()
                .message(message)
                .phoneNumber(environmentAdapter.getValueOrBust("phoneNumber"))
                .build()

        when (snsClient.publish(request).sdkHttpResponse().statusCode()) {
            200 -> println("Message succesfully sent.")
            else -> println("Message send failed.")
        }
    }
}