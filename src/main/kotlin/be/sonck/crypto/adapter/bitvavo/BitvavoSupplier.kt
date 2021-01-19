package be.sonck.crypto.adapter.bitvavo

import be.sonck.crypto.adapter.secrets.SecretsAdapter
import be.sonck.crypto.model.Account
import com.bitvavo.api.Bitvavo
import org.json.JSONObject

class BitvavoSupplier(
    private val secretsAdapter: SecretsAdapter = SecretsAdapter()
) {

    private val bitvavos: Map<Account, Bitvavo> = mapOf(
        Account.JOHAN to createBitvavo(Account.JOHAN),
        Account.ZEPPELLA to createBitvavo(Account.ZEPPELLA)
    )

    private fun createBitvavo(account: Account): Bitvavo {
        val apiKey = secretsAdapter.getValue("bitvavo", "api-key.${account.stringValue}")
        val apiSecret = secretsAdapter.getValue("bitvavo", "api-secret.${account.stringValue}")

        return Bitvavo(JSONObject("{ APIKEY: '$apiKey', APISECRET: '$apiSecret' }"))
    }

    fun get(account: Account) = bitvavos[account]!!
}