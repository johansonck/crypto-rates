package be.sonck.crypto.lambda

import be.sonck.crypto.adapter.secrets.SecretsAdapter
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler


@Suppress("unused")
class LambdaRequestHandler : RequestHandler<Any?, String?> {

    override fun handleRequest(input: Any?, context: Context): String? {
        println(SecretsAdapter().getValue("bitvavo", "api-key.johan"))
        return null
    }
}