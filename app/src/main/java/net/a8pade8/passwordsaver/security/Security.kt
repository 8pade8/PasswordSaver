package net.a8pade8.passwordsaver.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import net.a8pade8.passwordsaver.R
import net.a8pade8.passwordsaver.uiutil.middleToastLong
import java.io.IOException
import java.security.GeneralSecurityException
import java.security.SecureRandom

class Security private constructor(context: Context) {

    companion object {
        private const val USER_PASSWORD = "USER_PASSWORD"
        private const val CRYPTO_KEY = "CRYPTO_KEY"

        @Volatile
        private var INSTANCE: Security? = null

        fun getInstance(context: Context): Security {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: try {
                    Security(context).also { INSTANCE = it }
                } catch (e: GeneralSecurityException) {
                    middleToastLong(context, context.getString(R.string.ErrorAccessingTheApplicationSettingsFile))
                    e.printStackTrace()
                    throw RuntimeException("Failed to initialize Security", e)
                } catch (e: IOException) {
                    middleToastLong(context, context.getString(R.string.ErrorAccessingTheApplicationSettingsFile))
                    e.printStackTrace()
                    throw RuntimeException("Failed to initialize Security", e)
                }
            }
        }
    }

    private val sharedPreferences: SharedPreferences

    init {
        val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secret_shared_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun getPassword(): String {
        return sharedPreferences.getString(USER_PASSWORD, "") ?: ""
    }

    fun getCryptoKey(): String {
        val key = sharedPreferences.getString(CRYPTO_KEY, "")
        if (key.isNullOrEmpty()) {
            val generatedString = generateRandomKey()
            setCryptoKey(generatedString)
            return generatedString
        }
        return key
    }

    private fun setCryptoKey(generatedString: String) {
        sharedPreferences.edit().putString(CRYPTO_KEY, generatedString).apply()
    }

    fun setPassword(password: String) {
        sharedPreferences.edit().putString(USER_PASSWORD, password).apply()
    }

    private fun generateRandomKey(): String {
        val random = SecureRandom()
        val leftLimit = 48
        val rightLimit = 122
        val targetStringLength = 16
        val buffer = StringBuilder(targetStringLength)
        for (i in 0 until targetStringLength) {
            val randomLimitedInt = leftLimit + (random.nextFloat() * (rightLimit - leftLimit + 1)).toInt()
            buffer.append(randomLimitedInt.toChar())
        }
        return buffer.toString()
    }
}