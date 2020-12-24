package be.sonck.crypto.rates.adapter.ses

import be.sonck.crypto.rates.adapter.environment.EnvironmentAdapter
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sesv2.SesV2Client
import java.util.function.Consumer


@Suppress("NestedLambdaShadowedImplicitParameter")
class SesAdapter(
        private val sesClient: SesV2Client = SesV2Client.builder().region(Region.EU_WEST_1).build(),
        private val environmentAdapter: EnvironmentAdapter = EnvironmentAdapter()
): Consumer<String> {

    override fun accept(message: String) {
        sesClient.sendEmail { requestBuilder ->
            requestBuilder
                    .fromEmailAddress(environmentAdapter.getValueOrBust("fromEmailAddress"))
                    .destination { it.toAddresses(environmentAdapter.getValueOrBust("toEmailAddress")) }
                    .content { contentBuilder ->
                        contentBuilder.simple { messageBuilder ->
                            messageBuilder
                                    .subject { it.data("Cryptocurrency update") }
                                    .body { it.text { it.data(message) } }
                        }
                    }
        }
    }
}