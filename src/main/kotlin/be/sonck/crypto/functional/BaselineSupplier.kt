package be.sonck.crypto.functional

import be.sonck.crypto.adapter.bitvavo.BitvavoAdapter
import be.sonck.crypto.model.Account
import be.sonck.crypto.model.Coin
import java.math.BigDecimal
import java.math.RoundingMode

class BaselineSupplier(
    private val bitvavoAdapter: BitvavoAdapter = BitvavoAdapter()
) {

    fun get(account: Account, coin: Coin): BigDecimal? {
        return bitvavoAdapter.getInvestedAmount(account, coin)
            .let { invested ->
                if (invested <= BigDecimal.ZERO) null
                else {
                    val balance = bitvavoAdapter.getBalance(account, coin)
                    invested.divide(balance, 2, RoundingMode.HALF_EVEN)
                }
            }
    }
}

