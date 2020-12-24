package be.sonck.crypto.rates

import be.sonck.crypto.rates.adapter.environment.EnvironmentAdapter
import java.math.BigDecimal
import java.math.RoundingMode

class BaselineSupplier(
        private val environmentAdapter: EnvironmentAdapter = EnvironmentAdapter()
) {
    fun getBaseline(coin: Coin): BigDecimal {
        val euroInvested = environmentAdapter.getEuroInvestedInCoin(coin)
        val inWallet = environmentAdapter.getCoinsInWallet(coin)

        return BigDecimal(euroInvested).divide(BigDecimal(inWallet), 2, RoundingMode.HALF_EVEN)
    }
}

