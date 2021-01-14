package be.sonck.crypto.adapter.bitvavo

data class GetBalanceResponse(
    val symbol: String,
    val available: String,
    val inOrder: String
)