package be.sonck.crypto.adapter.environment

import be.sonck.crypto.functional.toTitleCase
import be.sonck.crypto.model.Coin

class EnvironmentAdapter {

    fun getValue(key: String, defaultValue: String? = null) = System.getenv(key) ?: defaultValue

    fun getValueOrBust(key: String, defaultValue: String? = null) =
            getValue(key, defaultValue) ?: throw IllegalStateException("missing value for environment property $key")

    fun getEuroInvestedInCoin(coin: Coin) = getValueOrBust("euroInvestedIn" + coin.name.toTitleCase())

    fun getCoinsInWallet(coin: Coin) = getValueOrBust(coin.name.toLowerCase() + "InWallet")
}