package be.sonck.crypto.functional

import be.sonck.crypto.adapter.bitvavo.BitvavoAdapter
import be.sonck.crypto.adapter.environment.EnvironmentAdapter
import be.sonck.crypto.adapter.ses.SesAdapter
import be.sonck.crypto.model.Account
import be.sonck.crypto.model.Coin
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.math.RoundingMode

class StopLossHandler(
    private val bitvavoAdapter: BitvavoAdapter = BitvavoAdapter(),
    private val baselineSupplier: BaselineSupplier = BaselineSupplier(),
    private val sesAdapter: SesAdapter = SesAdapter(),
    private val environmentAdapter: EnvironmentAdapter = EnvironmentAdapter(),
    private val log: Logger = LoggerFactory.getLogger(StopLossHandler::class.java)
) : Runnable {

    override fun run() {
        log.info("start")
        Coin.values().forEach { sellIfNoMoreProfit(coin = it) }
        log.info("done")
    }

    private fun sellIfNoMoreProfit(account: Account = Account.JOHAN, coin: Coin) {
        val balance = bitvavoAdapter.getBalance(account, coin) ?: return
        val stopLossPrice = getStopLossPrice(coin)
        val tickerPrice = bitvavoAdapter.getTickerPrice(account, coin)

        if (stopLossPrice < tickerPrice) {
            log.info("De prijs van ${coin.name} ($tickerPrice) ligt nog boven je stop-loss prijs ($stopLossPrice).")
            return
        }

        log.debug("sending mail for $coin")
        sesAdapter.accept("De prijs van ${coin.name} ($tickerPrice) is onder je stop-loss prijs ($stopLossPrice) gegaan. Alles wordt verkocht.")
        log.debug("sent mail for $coin")

        bitvavoAdapter.sell(account, coin, balance)
        log.warn("SOLD $coin $balance")
    }

    private fun getStopLossPrice(coin: Coin) =
        environmentAdapter.getValueOrBust("stopLossPrice$coin")
            .let { BigDecimal(it).setScale(2, RoundingMode.HALF_EVEN) }
}

fun main() {
    StopLossHandler().run()
}