package net.a8pade8.passwordsaver.util

import android.content.Context
import androidx.preference.PreferenceManager
import net.a8pade8.passwordsaver.data.addRecordToPasswords
import net.a8pade8.passwordsaver.security.Security

fun generateTestData(context: Context, generateTestData: Boolean) {

    if (!generateTestData) return

    val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val key = "testDataGenerated"
    val testDataGenerated = defaultSharedPreferences.getBoolean(key, false)
    if (!testDataGenerated) {
        Security.getInstance(context).setPassword("12345")
        addRecordToPasswords("vk.com", "unknown", "qwerty123", "Дополнительный", true)
        addRecordToPasswords("vk.com", "unknown2", "qwerty123")
        addRecordToPasswords("World of Warcraft", "unknown", "Asdfg321")
        addRecordToPasswords("World of Tanks", "unknown", "Asdfg321")
        addRecordToPasswords("mail.ru", "unknown", "qwerty123")
        addRecordToPasswords("yandex.ru", "unknown", "qwerty123")
        addRecordToPasswords("google.com", "unknown", "qwerty123")
        addRecordToPasswords("mos.ru", "unknown", "qwerty123")
        addRecordToPasswords("stepik.org", "unknown", "qwerty123", "Основной", true)
        addRecordToPasswords("github.com", "unknown", "qwerty123", "", true)
        defaultSharedPreferences.edit().putBoolean(key, true).apply()
    }
}