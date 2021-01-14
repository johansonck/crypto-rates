package be.sonck.crypto.functional

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class ExtensionsKtTest {

    @Test
    fun testFormat() {
        val property = System.getProperty("java.home")
        println("java.home=$property")
        assertThat(BigDecimal("1234.56").formatCurrency()).isEqualTo("€ 1.234,56")
    }
}