package be.sonck.crypto.functional

import be.sonck.crypto.model.Account
import be.sonck.crypto.model.Coin

class GetCryptoRatesHandler(
    private val messageDispatcher: MessageDispatcher = MessageDispatcher(),
    private val cryptoRateMessageFactory: CryptoRateMessageFactory = CryptoRateMessageFactory(),
    private val logHandler: (message: String) -> Unit = {}
) : Runnable {

    override fun run() {
        val message = Coin.values().joinToString("\n\n") {
            cryptoRateMessageFactory.create(Account.JOHAN, it)
        }

        logHandler.invoke("sending message $message")

        messageDispatcher.accept(message)
    }
}