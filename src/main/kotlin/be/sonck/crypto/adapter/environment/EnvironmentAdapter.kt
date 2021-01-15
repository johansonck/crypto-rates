package be.sonck.crypto.adapter.environment

class EnvironmentAdapter {

    fun getValue(key: String, defaultValue: String? = null) = System.getenv(key) ?: defaultValue

    fun getValueOrBust(key: String) =
            getValue(key) ?: throw IllegalStateException("missing value for environment property $key")
}