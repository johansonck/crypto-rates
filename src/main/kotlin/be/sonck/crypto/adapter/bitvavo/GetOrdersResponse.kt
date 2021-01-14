package be.sonck.crypto.adapter.bitvavo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GetOrdersResponse(
        val orderId: String,
        val side: String,
        val amountQuote: String? = null,
        val filledAmountQuote: String? = null,
        val market: String? = null,
        val feePaid: String? = null,
        val filledAmount: String? = null
)