package be.sonck.crypto.functional

import be.sonck.crypto.adapter.bitvavo.BitvavoAdapter
import be.sonck.crypto.model.Account
import be.sonck.crypto.model.Coin
import java.math.BigDecimal

class StopLossOrderCreator(
    private val bitvavoAdapter: BitvavoAdapter = BitvavoAdapter()
): Runnable {
    override fun run() {
        bitvavoAdapter.createStopLossOrder(Account.ZEPPELLA, Coin.BTC, BigDecimal("25000"))
    }
}

fun main() {
    StopLossOrderCreator().run()
}