package be.sonck.crypto.functional

import be.sonck.crypto.adapter.bitvavo.BitvavoAdapter
import be.sonck.crypto.adapter.cryptocompare.CryptoCompareAdapter
import be.sonck.crypto.adapter.environment.EnvironmentAdapter
import be.sonck.crypto.model.Coin
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.math.BigDecimal

@Disabled
internal class CryptoRateMessageFactoryTest {

    @Test
    fun testWinst() {
        val cryptoCompareAdapter = mockk<CryptoCompareAdapter>(relaxed = true)
        val messageDispatcher = mockk<MessageDispatcher>(relaxed = true)
        val environmentAdapter = mockk<EnvironmentAdapter>(relaxed = true)
        val bitvavoAdapter = mockk<BitvavoAdapter>(relaxed = true)

        every { cryptoCompareAdapter.getExchangeRate(Coin.BTC) } returns BigDecimal("19289.68")
        every { messageDispatcher.accept(any()) } answers { println("message: " + firstArg<String>()) }
        every { bitvavoAdapter.getInvestedAmount(Coin.BTC) } returns BigDecimal("1000")
        every { bitvavoAdapter.getBalance(Coin.BTC) } returns BigDecimal("0.26133722")

        val actualMessage = CryptoRateMessageFactory(
            cryptoCompareAdapter,
            BaselineSupplier(bitvavoAdapter)
        ).create(Coin.BTC)

        val expectedMessage =
            "De huidige waarde van 1 BTC is € 19.289,68. Dit is een stijging van 404% tov je baseline. De huidige winst is € 4.041,11."

        assertThat(actualMessage).isEqualTo(expectedMessage)
    }

    @Test
    fun testVerlies() {
        val cryptoCompareAdapter = mockk<CryptoCompareAdapter>(relaxed = true)
        val messageDispatcher = mockk<MessageDispatcher>(relaxed = true)
        val environmentAdapter = mockk<EnvironmentAdapter>(relaxed = true)
        val bitvavoAdapter = mockk<BitvavoAdapter>(relaxed = true)

        every { cryptoCompareAdapter.getExchangeRate(Coin.BTC) } returns BigDecimal("12289.68")
        every { messageDispatcher.accept(any()) } answers { println("message: " + firstArg<String>()) }
        every { bitvavoAdapter.getInvestedAmount(Coin.BTC) } returns BigDecimal("1000")
        every { bitvavoAdapter.getBalance(Coin.BTC) } returns BigDecimal("0.26133722")

        val actualMessage = CryptoRateMessageFactory(
            cryptoCompareAdapter,
            BaselineSupplier(bitvavoAdapter)
        ).create(Coin.BTC)

        val expectedMessage =
            "De huidige waarde van 1 BTC is € 12.289,68. Dit is een daling van 68% tov je baseline. Het huidige verlies is € 6.788,25."

        assertThat(actualMessage).isEqualTo(expectedMessage)
    }
}