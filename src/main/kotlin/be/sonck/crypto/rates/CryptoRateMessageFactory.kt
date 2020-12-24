package be.sonck.crypto.rates

import be.sonck.crypto.rates.adapter.cryptocompare.CryptoCompareAdapter
import be.sonck.crypto.rates.adapter.environment.EnvironmentAdapter
import java.math.BigDecimal
import java.math.RoundingMode

class CryptoRateMessageFactory(
        private val cryptoCompareAdapter: CryptoCompareAdapter = CryptoCompareAdapter(),
        private val baselineSupplier: BaselineSupplier = BaselineSupplier(),
        private val environmentAdapter: EnvironmentAdapter = EnvironmentAdapter()
) {
    fun create(coin: Coin): String {
        val exchangeRate = cryptoCompareAdapter.getExchangeRate(coin)
        val baseline = baselineSupplier.getBaseline(coin)
        val increaseFromBaseline = exchangeRate
                .divide(baseline, 2, RoundingMode.HALF_EVEN)
                .minus(BigDecimal.ONE)
                .multiply(BigDecimal("100"))
                .setScale(0, RoundingMode.HALF_EVEN)

        val profit = environmentAdapter.getCoinsInWallet(coin).toBigDecimal()
                .let { exchangeRate.multiply(it).setScale(2, RoundingMode.HALF_EVEN) }
                .minus(environmentAdapter.getEuroInvestedInCoin(coin).toBigDecimal())

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