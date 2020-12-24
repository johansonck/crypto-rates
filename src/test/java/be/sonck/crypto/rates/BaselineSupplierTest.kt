package be.sonck.crypto.rates

import be.sonck.crypto.rates.adapter.environment.EnvironmentAdapter
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class BaselineSupplierTest {

    private val environmentAdapter = mockk<EnvironmentAdapter>()

    @BeforeEach
    fun setup() {
        clearAllMocks()

        every { environmentAdapter.getEuroInvestedInCoin(Coin.BTC) } returns "1000"
        every { environmentAdapter.getCoinsInWallet(Coin.BTC) } returns "0.26133722"
        every { environmentAdapter.getEuroInvestedInCoin(Coin.ETH) } returns "1000"
        every { environmentAdapter.getCoinsInWallet(Coin.ETH) } returns "1.94905269"
    }

    @Test
    fun testBtc() {
        assertThat(BaselineSupplier(environmentAdapter).getBaseline(Coin.BTC)).isEqualTo(BigDecimal("3826.47"))
    }

    @Test
    fun testEth() {
        assertThat(BaselineSupplier(environmentAdapter).getBaseline(Coin.ETH)).isEqualTo(BigDecimal("513.07"))
    }
}