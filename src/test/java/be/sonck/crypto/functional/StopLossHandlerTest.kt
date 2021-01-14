package be.sonck.crypto.functional

import be.sonck.crypto.adapter.bitvavo.BitvavoAdapter
import be.sonck.crypto.adapter.ses.SesAdapter
import be.sonck.crypto.model.Coin
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class StopLossHandlerTest {

    @Test
    fun stillMakingProfit() {
        val bitvavoAdapter = mockk<BitvavoAdapter>(relaxed = true)
        val baselineSupplier = mockk<BaselineSupplier>(relaxed = true)
        val sesAdapter = mockk<SesAdapter>(relaxed = true)

        every { baselineSupplier.apply(Coin.BTC) }.returns(BigDecimal("15000.00"))
        every { baselineSupplier.apply(Coin.ETH) }.returns(BigDecimal("500.00"))
        every { bitvavoAdapter.getTickerPrice(Coin.BTC) }.returns(BigDecimal("29000.00"))
        every { bitvavoAdapter.getTickerPrice(Coin.ETH) }.returns(BigDecimal("900.00"))

        StopLossHandler(bitvavoAdapter, baselineSupplier, sesAdapter).run()

        verify(exactly = 0) { sesAdapter.accept(any()) }
    }

    @Test
    fun panicSellEth() {
        val bitvavoAdapter = mockk<BitvavoAdapter>(relaxed = true)
        val baselineSupplier = mockk<BaselineSupplier>(relaxed = true)
        val sesAdapter = mockk<SesAdapter>(relaxed = true)

        every { baselineSupplier.apply(Coin.BTC) }.returns(BigDecimal("15000.00"))
        every { baselineSupplier.apply(Coin.ETH) }.returns(BigDecimal("500.00"))
        every { bitvavoAdapter.getTickerPrice(Coin.BTC) }.returns(BigDecimal("29000.00"))
        every { bitvavoAdapter.getTickerPrice(Coin.ETH) }.returns(BigDecimal("540.00"))
        every { bitvavoAdapter.getBalance(Coin.ETH) }.returns(BigDecimal("1.23456789"))

        StopLossHandler(bitvavoAdapter, baselineSupplier, sesAdapter).run()

        verify(exactly = 1) { sesAdapter.accept(any()) }
        verify { bitvavoAdapter.sell(Coin.ETH, BigDecimal("1.23456789")) }
        verify(exactly = 0) { bitvavoAdapter.sell(Coin.BTC, any()) }
    }
}