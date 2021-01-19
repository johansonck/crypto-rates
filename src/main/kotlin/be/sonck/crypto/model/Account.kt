package be.sonck.crypto.model

enum class Account {
    JOHAN, ZEPPELLA;

    val stringValue: String
        get() = name.toLowerCase()
}