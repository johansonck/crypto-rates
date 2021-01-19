package be.sonck.crypto.adapter.bitvavo

import be.sonck.crypto.adapter.secrets.SecretsAdapter
import com.bitvavo.api.Bitvavo
import org.json.JSONObject
import java.util.function.Function

class BitvavoFactory(
    private val secretsAdapter: SecretsAdapter = SecretsAdapter()
) : Function<String, Bitvavo> {

    override fun apply(account: String): Bitvavo {
        val apiKey = secretsAdapter.getValue("bitvavo", "api-key.$account")
        val apiSecret = secretsAdapter.getValue("bitvavo", "api-secret.$account")

        return Bitvavo(JSONObject("{ APIKEY: '$apiKey', APISECRET: '$apiSecret' }"))
    }
}