package be.sonck.crypto.model

import java.math.BigDecimal

data class Order(
    val fiatAmount: BigDecimal,
    val coinAmount: BigDecimal,
    val coin: Coin
)