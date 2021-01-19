package be.sonck.crypto.functional

import be.sonck.crypto.adapter.bitvavo.BitvavoAdapter
import be.sonck.crypto.model.Account
import be.sonck.crypto.model.Coin
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class BaselineSupplierTest {

    private val bitvavoAdapter = mockk<BitvavoAdapter>()

    @BeforeEach
    fun setup() {
        clearAllMocks()

        every { bitvavoAdapter.getInvestedAmount(Account.JOHAN, Coin.BTC) } returns BigDecimal("1000")
        every { bitvavoAdapter.getBalance(Account.JOHAN, Coin.BTC) } returns BigDecimal("0.26133722")
        every { bitvavoAdapter.getInvestedAmount(Account.JOHAN, Coin.ETH) } returns BigDecimal("1000")
        every { bitvavoAdapter.getBalance(Account.JOHAN, Coin.ETH) } returns BigDecimal("1.94905269")
    }

    @Test
    fun testBtc() {
        Assertions.assertThat(BaselineSupplier(bitvavoAdapter).get(Account.JOHAN, Coin.BTC))
            .isEqualTo(BigDecimal("3826.47"))
    }

    @Test
    fun testEth() {
        Assertions.assertThat(BaselineSupplier(bitvavoAdapter).get(Account.JOHAN, Coin.ETH))
            .isEqualTo(BigDecimal("513.07"))
    }
}