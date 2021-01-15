package be.sonck.crypto.adapter.bitvavo

import be.sonck.crypto.adapter.environment.EnvironmentAdapter
import com.bitvavo.api.Bitvavo
import org.json.JSONObject
import java.util.function.Supplier

class BitvavoSupplier(
    environmentAdapter: EnvironmentAdapter = EnvironmentAdapter(),
    account: String = "Johan"
) : Supplier<Bitvavo> {

    private var bitvavo: Bitvavo

    init {
        val apiKey = environmentAdapter.getValueOrBust("bitvavo${account}ApiKey")
        val apiSecret = environmentAdapter.getValueOrBust("bitvavo${account}ApiSecret")

        bitvavo = Bitvavo(JSONObject("{ APIKEY: '$apiKey', APISECRET: '$apiSecret' }"))
    }

    override fun get() = bitvavo
}