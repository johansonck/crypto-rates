package be.sonck.crypto.adapter.bitvavo

import be.sonck.crypto.model.Coin
import com.bitvavo.api.Bitvavo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.json.JSONObject
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class BitvavoAdapterTest {

    @Test
    @Disabled
    fun testSell() {
        val bitvavo = mockk<Bitvavo>(relaxed = true)
        val bitvavoFactory = mockk<BitvavoFactory>(relaxed = true)

        every { bitvavoFactory.get() }.returns(bitvavo)

        BitvavoAdapter(bitvavoFactory = bitvavoFactory).sell(Coin.BTC, BigDecimal("0.00000200"))

        verify { bitvavo.placeOrder("BTC-EUR", "sell", "market", JSONObject().put("amount", "0.00000200")) }
    }
}