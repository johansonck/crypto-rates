package be.sonck.crypto.adapter.dynamodb

import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.util.function.Supplier

class DynamoDbClientSupplier : Supplier<DynamoDbClient> {

    lateinit var client: DynamoDbClient

    constructor() {
        client = DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .build()
    }


    override fun get() = client
}