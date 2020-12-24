package be.sonck.crypto.rates

import be.sonck.crypto.rates.adapter.cryptocompare.CryptoCompareAdapter
import be.sonck.crypto.rates.adapter.environment.EnvironmentAdapter
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class CryptoRateMessageFactoryTest {

    @Test
    fun testWinst() {
        val cryptoCompareAdapter = mockk<CryptoCompareAdapter>(relaxed = true)
        val messageDispatcher = mockk<MessageDispatcher>(relaxed = true)
        val environmentAdapter = mockk<EnvironmentAdapter>(relaxed = true)

        every { cryptoCompareAdapter.getExchangeRate(Coin.BTC) } returns BigDecimal("19289.68")
        every { messageDispatcher.accept(any()) } answers { println("message: " + firstArg<String>()) }
        every { environmentAdapter.getEuroInvestedInCoin(Coin.BTC) } returns "1000"
        every { environmentAdapter.getCoinsInWallet(Coin.BTC) } returns "0.26133722"

        val actualMessage = CryptoRateMessageFactory(
                cryptoCompareAdapter,
                BaselineSupplier(environmentAdapter),
                environmentAdapter
        ).create(Coin.BTC)

        val expectedMessage = "De huidige waarde van 1 BTC is € 19.289,68. Dit is een stijging van 404% tov je baseline. De huidige winst is € 4.041,11."

        assertThat(actualMessage).isEqualTo(expectedMessage)
    }

    @Test
    fun testVerlies() {
        val cryptoCompareAdapter = mockk<CryptoCompareAdapter>(relaxed = true)
        val messageDispatcher = mockk<MessageDispatcher>(relaxed = true)
        val environmentAdapter = mockk<EnvironmentAdapter>(relaxed = true)

        every { cryptoCompareAdapter.getExchangeRate(Coin.BTC) } returns BigDecimal("12289.68")
        every { messageDispatcher.accept(any()) } answers { println("message: " + firstArg<String>()) }
        every { environmentAdapter.getEuroInvestedInCoin(Coin.BTC) } returns "10000"
        every { environmentAdapter.getCoinsInWallet(Coin.BTC) } returns "0.26133722"

        val actualMessage = CryptoRateMessageFactory(
                cryptoCompareAdapter,
                BaselineSupplier(environmentAdapter),
                environmentAdapter
        ).create(Coin.BTC)

        val expectedMessage = "De huidige waarde van 1 BTC is € 12.289,68. Dit is een daling van 68% tov je baseline. Het huidige verlies is € 6.788,25."

        assertThat(actualMessage).isEqualTo(expectedMessage)
    }
}