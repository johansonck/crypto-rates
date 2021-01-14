package be.sonck.crypto.functional

import be.sonck.crypto.adapter.bitvavo.BitvavoAdapter
import be.sonck.crypto.model.Coin
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.function.Function

class CurrentValueSupplier(
    private val bitvavoAdapter: BitvavoAdapter = BitvavoAdapter()
): Function<Coin, BigDecimal> {

    override fun apply(coin: Coin): BigDecimal {
        val tickerPrice = bitvavoAdapter.getTickerPrice(coin)
        val balance = bitvavoAdapter.getBalance(coin)

        return tickerPrice.multiply(balance).setScale(2, RoundingMode.HALF_EVEN)
    }
}