package be.sonck.crypto.functional

import be.sonck.crypto.adapter.bitvavo.BitvavoAdapter
import be.sonck.crypto.adapter.environment.EnvironmentAdapter
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
    private val environmentAdapter: EnvironmentAdapter = EnvironmentAdapter(),
    private val log: Logger = LoggerFactory.getLogger(StopLossHandler::class.java)
) : Runnable {

    override fun run() {
        log.info("start")
        listOf(Coin.BTC, Coin.ETH).forEach { sellIfNoMoreProfit(it) }
        log.info("done")
    }

    private fun sellIfNoMoreProfit(coin: Coin) {
        val stopLossPrice = getStopLossPrice(coin) ?: return
        val tickerPrice = bitvavoAdapter.getTickerPrice(coin)

        if (stopLossPrice < tickerPrice) {
            log.info("De prijs van ${coin.name} ($tickerPrice) ligt nog boven je stop-loss prijs ($stopLossPrice).")
        } else {
            log.debug("sending mail for $coin")
            sesAdapter.accept("De prijs van ${coin.name} ($tickerPrice) is onder je stop-loss prijs ($stopLossPrice) gegaan. Alles wordt verkocht.")
            log.debug("sent mail for $coin")

            bitvavoAdapter.getBalance(coin)
                ?.also { balance ->
                    bitvavoAdapter.sell(coin, balance)
                    log.warn("SOLD $coin $balance")
                }
        }
    }

    private fun getStopLossPrice(coin: Coin): BigDecimal? {
        return baselineSupplier.apply(coin)
            ?.multiply(BigDecimal.ONE.plus(minimumProfit().asPercentage()))
            ?.setScale(2, RoundingMode.HALF_EVEN)
    }

    private fun BigDecimal.asPercentage() = divide(BigDecimal(100), 2, RoundingMode.HALF_EVEN)

    private fun minimumProfit() = BigDecimal(environmentAdapter.getValueOrBust("minimumProfit"))
}

fun main() {
    StopLossHandler().run()
}