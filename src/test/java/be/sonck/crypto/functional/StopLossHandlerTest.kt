package be.sonck.crypto.functional

import be.sonck.crypto.adapter.bitvavo.BitvavoAdapter
import be.sonck.crypto.adapter.environment.EnvironmentAdapter
import be.sonck.crypto.adapter.ses.SesAdapter
import be.sonck.crypto.model.Coin
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class StopLossHandlerTest {

    private val bitvavoAdapter = mockk<BitvavoAdapter>(relaxed = true)
    private val baselineSupplier = mockk<BaselineSupplier>(relaxed = true)
    private val sesAdapter = mockk<SesAdapter>(relaxed = true)
    private val environmentAdapter = mockk<EnvironmentAdapter>(relaxed = true)

    private lateinit var stopLossHandler: StopLossHandler

    @BeforeEach
    fun setup() {
        every { environmentAdapter.getValueOrBust("minimumProfit") }.returns("25")

        stopLossHandler = StopLossHandler(bitvavoAdapter, baselineSupplier, sesAdapter, environmentAdapter)
    }

    @Test
    fun stillMakingProfit() {
        every { baselineSupplier.apply(Coin.BTC) }.returns(BigDecimal("15000.00"))
        every { baselineSupplier.apply(Coin.ETH) }.returns(BigDecimal("500.00"))
        every { bitvavoAdapter.getTickerPrice(Coin.BTC) }.returns(BigDecimal("29000.00"))
        every { bitvavoAdapter.getTickerPrice(Coin.ETH) }.returns(BigDecimal("900.00"))

        stopLossHandler.run()

        verify(exactly = 0) { sesAdapter.accept(any()) }
    }

    @Test
    fun sellAllEth() {
        every { baselineSupplier.apply(Coin.BTC) }.returns(BigDecimal("15000.00"))
        every { baselineSupplier.apply(Coin.ETH) }.returns(BigDecimal("500.00"))
        every { bitvavoAdapter.getTickerPrice(Coin.BTC) }.returns(BigDecimal("29000.00"))
        every { bitvavoAdapter.getTickerPrice(Coin.ETH) }.returns(BigDecimal("540.00"))
        every { bitvavoAdapter.getBalance(Coin.ETH) }.returns(BigDecimal("1.23456789"))

        stopLossHandler.run()

        verify(exactly = 1) { sesAdapter.accept(any()) }
        verify { bitvavoAdapter.sell(Coin.ETH, BigDecimal("1.23456789")) }
        verify(exactly = 0) { bitvavoAdapter.sell(Coin.BTC, any()) }
    }
}