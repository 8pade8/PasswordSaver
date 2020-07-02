package net.a8pade8.passwordsaver.util

import android.content.Context
import android.preference.PreferenceManager
import net.a8pade8.passwordsaver.security.Security
import net.a8pade8.passwordsaver.data.addRecordToPasswords

fun generateTestData(context: Context, generateTestData: Boolean) {

    if (!generateTestData) return

    val TEST_DATA_GENERATED = "testDataGenerated"

    val defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val testDataGenerated = defaultSharedPreferences.getBoolean(TEST_DATA_GENERATED, false)
    if (!testDataGenerated){
        Security.getInstance(context).password = "12345"
        addRecordToPasswords("vk.com", "unknown", "qwerty123")
        addRecordToPasswords("vk.com", "unknown2", "qwerty123")
        addRecordToPasswords("World of Warcraft", "unknown", "Asdfg321")
        addRecordToPasswords("World of Tanks", "unknown", "Asdfg321")
        addRecordToPasswords("mail.ru", "unknown", "qwerty123")
        addRecordToPasswords("yandex.ru", "unknown", "qwerty123")
        addRecordToPasswords("google.com", "unknown", "qwerty123")
        addRecordToPasswords("mos.ru", "unknown", "qwerty123")
        addRecordToPasswords("stepik.org", "unknown", "qwerty123")
        addRecordToPasswords("github.com", "unknown", "qwerty123")
        defaultSharedPreferences.edit().putBoolean(TEST_DATA_GENERATED, true).apply()
    }
}