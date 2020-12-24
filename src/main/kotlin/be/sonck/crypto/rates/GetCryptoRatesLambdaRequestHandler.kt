package be.sonck.crypto.rates

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler


@Suppress("unused")
class GetCryptoRatesLambdaRequestHandler : RequestHandler<Any?, String?> {

    override fun handleRequest(input: Any?, context: Context): String {
        GetCryptoRatesHandler(logHandler = { context.logger.log("$it\n") }).run()
        return "Done"
    }
}