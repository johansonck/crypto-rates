package be.sonck.crypto.adapter.bitvavo

import be.sonck.crypto.adapter.environment.EnvironmentAdapter
import com.bitvavo.api.Bitvavo
import org.json.JSONObject
import java.util.function.Supplier

class BitvavoFactory(
        private val environmentAdapter: EnvironmentAdapter = EnvironmentAdapter()
): Supplier<Bitvavo> {

    override fun get(): Bitvavo {
        val apiKey = environmentAdapter.getValueOrBust("bitvavoJohanApiKey")
        val apiSecret = environmentAdapter.getValueOrBust("bitvavoJohanApiSecret")

        return Bitvavo(JSONObject("{ APIKEY: '$apiKey', APISECRET: '$apiSecret' }"))
    }
}