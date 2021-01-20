package be.sonck.crypto.functional

import be.sonck.crypto.adapter.bitvavo.BitvavoAdapter
import be.sonck.crypto.adapter.environment.EnvironmentAdapter
import be.sonck.crypto.adapter.ses.SesAdapter
import be.sonck.crypto.model.Account
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
        every { environmentAdapter.getValueOrBust("stopLossPriceBTC") }.returns("25000")
        every { environmentAdapter.getValueOrBust("stopLossPriceETH") }.returns("750")

        stopLossHandler = StopLossHandler(bitvavoAdapter, baselineSupplier, sesAdapter, environmentAdapter)
    }

    @Test
    fun stillMakingProfit() {
        every { bitvavoAdapter.getTickerPrice(Account.JOHAN, Coin.BTC) }.returns(BigDecimal("29000.00"))
        every { bitvavoAdapter.getTickerPrice(Account.JOHAN, Coin.ETH) }.returns(BigDecimal("900.00"))

        stopLossHandler.run()

        verify(exactly = 0) { sesAdapter.accept(any()) }
    }

    @Test
    fun sellAllEth() {
        every { bitvavoAdapter.getTickerPrice(Account.JOHAN, Coin.BTC) }.returns(BigDecimal("26000.00"))
        every { bitvavoAdapter.getTickerPrice(Account.JOHAN, Coin.ETH) }.returns(BigDecimal("540.00"))
        every { bitvavoAdapter.getBalance(Account.JOHAN, Coin.ETH) }.returns(BigDecimal("1.23456789"))

        stopLossHandler.run()

        verify(exactly = 1) { sesAdapter.accept(any()) }
        verify { bitvavoAdapter.sell(Account.JOHAN, Coin.ETH, BigDecimal("1.23456789")) }
        verify(exactly = 0) { bitvavoAdapter.sell(Account.JOHAN, Coin.BTC, any()) }
    }
}