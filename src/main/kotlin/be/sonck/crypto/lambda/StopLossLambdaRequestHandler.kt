package be.sonck.crypto.lambda

import be.sonck.crypto.functional.StopLossHandler
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler

class StopLossLambdaRequestHandler : RequestHandler<Any?, String?> {
    override fun handleRequest(input: Any?, context: Context?): String? {
        StopLossHandler().run()
        return null
    }
}
