package be.sonck.crypto.functional

import be.sonck.crypto.adapter.bitvavo.BitvavoAdapter
import be.sonck.crypto.model.Coin
import java.math.BigDecimal
import java.math.RoundingMode

class CryptoRateMessageFactory(
    private val baselineSupplier: BaselineSupplier = BaselineSupplier(),
    private val bitvavoAdapter: BitvavoAdapter = BitvavoAdapter()
) {
    fun create(coin: Coin): String {
        val exchangeRate = bitvavoAdapter.getTickerPrice(coin)
        val baseline = baselineSupplier.apply(coin)
        val increaseFromBaseline = exchangeRate
                .divide(baseline, 2, RoundingMode.HALF_EVEN)
                .minus(BigDecimal.ONE)
                .multiply(BigDecimal("100"))
                .setScale(0, RoundingMode.HALF_EVEN)

        val profit = bitvavoAdapter.getBalance(coin)
                .let { exchangeRate.multiply(it).setScale(2, RoundingMode.HALF_EVEN) }
                .minus(bitvavoAdapter.getInvestedAmount(coin))

        return "De huidige waarde van 1 ${coin.name} is ${exchangeRate.formatCurrency()}. " +
                "Dit is een " +
                percentageText(increaseFromBaseline) +
                "% tov je baseline. " +
                profitText(profit)
    }

    private fun profitText(profit: BigDecimal) =
            if (profit > BigDecimal.ZERO) "De huidige winst is ${profit.formatCurrency()}."
            else "Het huidige verlies is ${profit.multiply(BigDecimal(-1)).formatCurrency()}."

    private fun percentageText(increaseFromBaseline: BigDecimal) =
            if (increaseFromBaseline > BigDecimal.ZERO) "stijging van $increaseFromBaseline"
            else "daling van ${increaseFromBaseline.multiply(BigDecimal(-1))}"
}