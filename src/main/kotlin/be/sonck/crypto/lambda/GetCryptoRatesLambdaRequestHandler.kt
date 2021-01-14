package be.sonck.crypto.lambda

import be.sonck.crypto.functional.GetCryptoRatesHandler
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler


@Suppress("unused")
class GetCryptoRatesLambdaRequestHandler : RequestHandler<Any?, String?> {

    override fun handleRequest(input: Any?, context: Context): String {
        GetCryptoRatesHandler(logHandler = { context.logger.log("$it\n") }).run()
        return "Done"
    }
}