package be.sonck.crypto.rates.adapter.cryptocompare

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class Rates(@JsonProperty("EUR") val euroRate: BigDecimal)
