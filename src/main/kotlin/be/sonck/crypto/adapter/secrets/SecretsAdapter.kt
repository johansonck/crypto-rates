package be.sonck.crypto.adapter.secrets

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest


class SecretsAdapter(
    private val objectMapper: ObjectMapper = jacksonObjectMapper(),
    private val secretsManagerClient: SecretsManagerClient =
        SecretsManagerClient.builder().region(Region.EU_WEST_1).build(),
) {

    fun getValue(secretName: String, secretKey: String): String =
        getSecrets(secretName)[secretKey]
            ?: throw IllegalArgumentException("no secret found for name $secretName and key $secretKey")

    private fun getSecrets(secretName: String): Map<String, String> =
        secretsManagerClient.getSecretValue(valueRequest(secretName)).secretString()
            .let { objectMapper.readValue(it.toString()) }

    private fun valueRequest(secretName: String) = GetSecretValueRequest.builder().secretId(secretName).build()
}