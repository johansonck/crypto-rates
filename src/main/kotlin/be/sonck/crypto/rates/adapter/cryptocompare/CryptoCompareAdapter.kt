package be.sonck.crypto.rates.adapter.cryptocompare

import be.sonck.crypto.rates.Coin
import be.sonck.crypto.rates.adapter.environment.EnvironmentAdapter
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal

class CryptoCompareAdapter(
        private val environmentAdapter: EnvironmentAdapter = EnvironmentAdapter(),
        private val restTemplate: RestTemplate = RestTemplate()
) {
    fun getExchangeRate(coin: Coin): BigDecimal =
            restTemplate.getForEntity(
                    "https://min-api.cryptocompare.com/data/price?fsym=${coin.name}&tsyms=EUR",
                    Rates::class.java,
                    mapOf("api_key" to environmentAdapter.getValue("cryptoCompareApiKey", null))
            ).run {
                if (statusCode != HttpStatus.OK) {
                    throw IllegalStateException("GetCryptoRates failure: response code from cryptocompare is $statusCode")
                }

                if (!hasBody()) {
                    throw IllegalStateException("GetCryptoRates failure: response body from cryptocompare is empty")
                }

                body!!.euroRate
            }
}