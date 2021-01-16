package be.sonck.crypto.lambda

import be.sonck.crypto.adapter.ses.SesAdapter
import be.sonck.crypto.functional.StopLossHandler
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler

@Suppress("unused")
class StopLossLambdaRequestHandler : RequestHandler<Any?, String?> {
    override fun handleRequest(input: Any?, context: Context?): String? {
        try {
            StopLossHandler().run()
        } catch (e: Exception) {
            SesAdapter().accept("An error occurred: ${e.message} [${e.stackTraceToString()}]")
            throw e
        }
        return null
    }
}
