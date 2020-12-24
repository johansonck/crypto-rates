package be.sonck.crypto.rates

class GetCryptoRatesHandler(
        private val messageDispatcher: MessageDispatcher = MessageDispatcher(),
        private val cryptoRateMessageFactory: CryptoRateMessageFactory = CryptoRateMessageFactory(),
        private val logHandler: (message: String) -> Unit = {}
) : Runnable {

    override fun run() {
        val message = listOf(Coin.BTC, Coin.ETH).joinToString("\n\n") {
            cryptoRateMessageFactory.create(it)
        }

        logHandler.invoke("sending message $message")

        messageDispatcher.accept(message)
    }
}