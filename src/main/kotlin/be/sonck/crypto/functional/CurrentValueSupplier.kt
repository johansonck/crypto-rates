package be.sonck.crypto.functional

import be.sonck.crypto.adapter.bitvavo.BitvavoAdapter
import be.sonck.crypto.model.Account
import be.sonck.crypto.model.Coin
import java.math.BigDecimal
import java.math.RoundingMode

class CurrentValueSupplier(
    private val bitvavoAdapter: BitvavoAdapter = BitvavoAdapter()
) {

    fun get(account: Account, coin: Coin): BigDecimal {
        val tickerPrice = bitvavoAdapter.getTickerPrice(account, coin)
        val balance = bitvavoAdapter.getBalance(account, coin)

        return tickerPrice.multiply(balance).setScale(2, RoundingMode.HALF_EVEN)
    }
}