package be.sonck.crypto.functional

import be.sonck.crypto.adapter.bitvavo.BitvavoAdapter
import be.sonck.crypto.adapter.ses.SesAdapter
import be.sonck.crypto.model.Coin
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.math.RoundingMode

class StopLossHandler(
    private val bitvavoAdapter: BitvavoAdapter = BitvavoAdapter(),
    private val baselineSupplier: BaselineSupplier = BaselineSupplier(),
    private val sesAdapter: SesAdapter = SesAdapter(),
    private val log: Logger = LoggerFactory.getLogger(StopLossHandler::class.java)
) : Runnable {

    companion object {
        const val MINIMUM_PROFIT = 25
    }

    override fun run() {
        log.info("start")
        listOf(Coin.BTC, Coin.ETH).forEach { sellIfNoMoreProfit(it) }
        log.info("done")
    }

    fun sellIfNoMoreProfit(coin: Coin) {
        val baseline = (baselineSupplier.apply(coin) ?: return)
            .apply { log.debug("baseline=$this") }
        val tickerPrice = bitvavoAdapter.getTickerPrice(coin)
            .apply { log.debug("tickerPrice=$this") }
        val stopLossPrice = baseline.multiply(
            BigDecimal.ONE.plus(
                BigDecimal(MINIMUM_PROFIT).divide(BigDecimal(100), 2, RoundingMode.HALF_EVEN)
            )
        ).setScale(2, RoundingMode.HALF_EVEN)
            .apply { log.debug("stopLossPrice=$this") }

        if (stopLossPrice >= tickerPrice) {
            log.debug("sending mail for $coin")
//            sesAdapter.accept("De prijs van ${coin.name} ($tickerPrice) is onder je stop-loss prijs ($stopLossPrice) gegaan. Alles wordt verkocht.")
            log.debug("sent mail for $coin")
//            bitvavoAdapter.sell(coin, bitvavoAdapter.getBalance(coin))
        }
    }
}

fun main() {
    StopLossHandler().run()
}