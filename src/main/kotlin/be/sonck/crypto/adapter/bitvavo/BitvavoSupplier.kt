package be.sonck.crypto.adapter.bitvavo

import be.sonck.crypto.adapter.secrets.SecretsAdapter
import com.bitvavo.api.Bitvavo
import org.json.JSONObject
import java.util.function.Supplier

class BitvavoSupplier(
    secretsAdapter: SecretsAdapter = SecretsAdapter(),
    account: String = "johan"
) : Supplier<Bitvavo> {

    private var bitvavo: Bitvavo

    init {
        val apiKey = secretsAdapter.getValue("bitvavo", "api-key.$account")
        val apiSecret = secretsAdapter.getValue("bitvavo", "api-secret.$account")

        bitvavo = Bitvavo(JSONObject("{ APIKEY: '$apiKey', APISECRET: '$apiSecret' }"))
    }

    override fun get() = bitvavo
}