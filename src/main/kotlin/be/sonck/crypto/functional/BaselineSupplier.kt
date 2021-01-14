package be.sonck.crypto.functional

import be.sonck.crypto.adapter.bitvavo.BitvavoAdapter
import be.sonck.crypto.model.Coin
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.function.Function

class BaselineSupplier(
    private val bitvavoAdapter: BitvavoAdapter = BitvavoAdapter()
) : Function<Coin, BigDecimal?> {

    override fun apply(coin: Coin): BigDecimal? {
        return bitvavoAdapter.getInvestedAmount(coin)
            .let { invested ->
                if (invested <= BigDecimal.ZERO) null
                else {
                    val balance = bitvavoAdapter.getBalance(coin)
                    invested.divide(balance, 2, RoundingMode.HALF_EVEN)
                }
            }
    }
}

