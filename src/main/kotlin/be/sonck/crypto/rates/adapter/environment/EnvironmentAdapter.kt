package be.sonck.crypto.rates.adapter.environment

import be.sonck.crypto.rates.Coin
import be.sonck.crypto.rates.toTitleCase

class EnvironmentAdapter {

    fun getValue(key: String, defaultValue: String? = null) = System.getenv(key) ?: defaultValue

    fun getValueOrBust(key: String, defaultValue: String? = null) =
            getValue(key, defaultValue) ?: throw IllegalStateException("missing value for environment property $key")

    fun getEuroInvestedInCoin(coin: Coin) = getValueOrBust("euroInvestedIn" + coin.name.toTitleCase())

    fun getCoinsInWallet(coin: Coin) = getValueOrBust(coin.name.toLowerCase() + "InWallet")
}