package be.sonck.crypto.adapter.bitvavo

import be.sonck.crypto.model.Account
import be.sonck.crypto.model.Coin
import be.sonck.crypto.model.Order
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode


class BitvavoAdapter(
    private val objectMapper: ObjectMapper = jacksonObjectMapper(),
    private val bitvavoSupplier: BitvavoSupplier = BitvavoSupplier()
) {

    fun getOrders(account: Account, coin: Coin): List<Order> =
        bitvavo(account)
            .getOrders("$coin-EUR", JSONObject())
            .let { objectMapper.readValue<List<GetOrdersResponse>>(it.toString()) }
            .map {
                Order(
                    fiatAmount =
                    if (it.side == "buy") bigDecimal(it.amountQuote ?: "0", 2)
                    else bigDecimal(it.filledAmountQuote ?: "0", 2).negate(),
                    coinAmount =
                    if (it.side == "buy") bigDecimal(it.filledAmount ?: "0", 8)
                    else bigDecimal(it.filledAmount ?: "0", 8).negate(),
                    coin = coin
                )
            }

    fun getInvestedAmount(account: Account, coin: Coin): BigDecimal =
        getOrders(account, coin)
            .sumOf { it.fiatAmount.setScale(2, RoundingMode.HALF_EVEN) }

    fun getBalance(account: Account, coin: Coin): BigDecimal? =
        bitvavo(account)
            .balance(JSONObject().put("symbol", coin.name))
            .let { objectMapper.readValue<List<GetBalanceResponse>>(it.toString()) }
            .map { bigDecimal(it.available, 8) }
            .firstOrNull()

    fun getTickerPrice(account: Account, coin: Coin): BigDecimal =
        bitvavo(account)
            .tickerPrice(JSONObject().put("market", coin.name + "-EUR"))
            .let { objectMapper.readValue<List<GetTickerPriceResponse>>(it.toString()) }
            .map { it.price.setScale(2, RoundingMode.HALF_EVEN) }
            .firstOrNull() ?: throw IllegalArgumentException("failed to find ticker price for $coin")

    fun sell(account: Account, coin: Coin, amount: BigDecimal) {
        val response = bitvavo(account)
            .placeOrder(
                coin.name + "-EUR",
                "sell",
                "market",
                JSONObject().put("amount", amount.toPlainString())
            )

        println(response)
    }

    private fun bigDecimal(value: String, scale: Int) =
        BigDecimal(value).setScale(scale, RoundingMode.HALF_EVEN)

    private fun bitvavo(account: Account) = bitvavoSupplier.get(account)
}