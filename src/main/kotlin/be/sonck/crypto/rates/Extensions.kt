package be.sonck.crypto.rates

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

fun String.toTitleCase() =
        if (this.length <= 1) this.toUpperCase()
        else this[0].toUpperCase() + this.substring(1).toLowerCase()

fun BigDecimal.formatCurrency(): String = NumberFormat.getCurrencyInstance(Locale("nl", "BE"))
        .format(this)
        .replace("\u00A0", " ")
