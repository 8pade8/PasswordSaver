package net.a8pade8.passwordsaver.util

import java.util.regex.Pattern
import kotlin.random.Random

val symbolsArray = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
val althaNumericRegex = "[0-9a-zA-Z]*((\\d+[a-zA-Z]+)|([a-zA-Z]+\\d+))[0-9a-zA-Z]*"

fun generateAlthaNumericString(length: Int): String {
    val stringBuilder: StringBuilder = StringBuilder()
    for (i: Int in 1..length) {
        stringBuilder.append(symbolsArray[Random.nextInt(0, symbolsArray.length - 1)])
    }
    val string = stringBuilder.toString()
    return if (!isAlthaNumeric(string)) {
        generateAlthaNumericString(length)
    } else {
        string
    }
}

fun isAlthaNumeric(string: String): Boolean {
    return Pattern.matches(althaNumericRegex, string)
}