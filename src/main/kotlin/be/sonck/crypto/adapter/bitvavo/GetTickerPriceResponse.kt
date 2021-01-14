package be.sonck.crypto.adapter.bitvavo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal

@JsonIgnoreProperties(ignoreUnknown = true)
data class GetTickerPriceResponse(
        val market: String,
        val price: BigDecimal
)