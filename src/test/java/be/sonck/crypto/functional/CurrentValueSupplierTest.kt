package be.sonck.crypto.functional

import be.sonck.crypto.adapter.bitvavo.BitvavoAdapter
import be.sonck.crypto.model.Coin
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class CurrentValueSupplierTest {

    @Test
    fun test() {
        val bitvavoAdapter = mockk<BitvavoAdapter>()

        every { bitvavoAdapter.getTickerPrice(Coin.BTC) }.returns(BigDecimal("23456.78"))
        every { bitvavoAdapter.getBalance(Coin.BTC) }.returns(BigDecimal("0.3"))

        assertThat(CurrentValueSupplier(bitvavoAdapter).apply(Coin.BTC))
            .isEqualTo(BigDecimal("7037.03"))
    }
}