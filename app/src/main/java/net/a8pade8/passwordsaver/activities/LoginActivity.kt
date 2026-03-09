package net.a8pade8.passwordsaver.activities

import android.Manifest
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.AuthenticationCallback
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import net.a8pade8.passwordsaver.R
import net.a8pade8.passwordsaver.data.loading
import net.a8pade8.passwordsaver.databinding.ActivityLoginBinding
import net.a8pade8.passwordsaver.security.Security
import net.a8pade8.passwordsaver.uiutil.middleToastLong
import net.a8pade8.passwordsaver.uiutil.middleToastShort
import net.a8pade8.passwordsaver.uiutil.showShortSnack
import net.a8pade8.passwordsaver.util.finishAndOpenActivity
import net.a8pade8.passwordsaver.util.generateTestData
import net.a8pade8.passwordsaver.util.openActivity
import java.util.concurrent.Executor


class LoginActivity : AppCompatActivity() {
    private val generateTestData = false // Генерировать тестовые данные
    private lateinit var security: Security
    private lateinit var binding: ActivityLoginBinding
    private var attemptPassword = 3
    private lateinit var preferences: SharedPreferences
    private lateinit var preferencesEditor: SharedPreferences.Editor
    private val BLOCK_TIME = "blockTime"
    private val ATTEMPT_COUNT = "attempts"
    private val authenticationCallback = object : AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            middleToastLong(this@LoginActivity, getString(R.string.fingerError) + ":$errString")
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            finishAndOpenActivity(MainActivity::class.java)
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            middleToastLong(this@LoginActivity, getString(R.string.fingerError))
        }
    }
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: PromptInfo

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        security = Security.getInstance(this)
        setContentView(R.layout.activity_login)
        loading(this)
        generateTestData(this, generateTestData) //Генерация тестовых данных
        if (security.password.isEmpty()) {
            openAddUserActivity(null)
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        preferencesEditor = preferences.edit()
        if (preferences.contains(ATTEMPT_COUNT)) {
            attemptPassword = preferences.getInt(ATTEMPT_COUNT, 0)
        }
        if (attemptPassword == 0) {
            attemptPassword = 3
        }
        if (isBlocked()) {
            attemptPassword = 0
        }

        // --- 1. Create an Executor ---
        // The executor is used to run the authentication on the main thread.
        executor = ContextCompat.getMainExecutor(this)

        // --- 2. Set up the BiometricPrompt Callbacks ---
        biometricPrompt = BiometricPrompt(this, executor, authenticationCallback)

        // --- 3. Configure the PromptInfo (UI dialog) ---
        promptInfo = PromptInfo.Builder()
            .setTitle("Biometric authentication")
            .setSubtitle("Log in using your fingerprint")
            .setNegativeButtonText("Cancel") // A non-biometric fallback (e.g., PIN) can also be set
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG) // Enforce strong biometrics
            .build()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun openAddUserActivity(view: View?) {
        openActivity(AddUserActivity::class.java)
    }

    @Suppress("UNUSED_PARAMETER")
    fun fingerAuthenticate(view: View?) {
        ActivityCompat.requestPermissions(this,  arrayOf<String>(USE_BIOMETRIC), 1)
        biometricPrompt.authenticate(promptInfo)
    }

    @Suppress("UNUSED_PARAMETER")
    fun openMainActivity(view: View?) {
        if (isAttemptExist() && isPasswordExist()) {
            finishAndOpenActivity(MainActivity::class.java)
        } else {
            binding.password = ""
        }
    }

    private fun isPasswordExist(): Boolean {
        if (security.password != binding.password) {
            showShortSnack(getString(R.string.attemptsWarning) + --attemptPassword)
            if (attemptPassword == 0) {
                if (!isBlocked()) {
                    block()
                }
            }
            return false
        }
        return true
    }

    private fun isAttemptExist(): Boolean {
        if (attemptPassword == 0) {
            showShortSnack(getString(R.string.noAttempts))
            binding.password = ""
            return false
        }
        return true
    }

    private fun isBlocked(): Boolean {
        if (preferences.contains(BLOCK_TIME)) {
            val blockTime = preferences.getLong("blockTime", 0)
            val timeNow = System.currentTimeMillis()
            return (timeNow - blockTime) / 60000 <= 5
        }
        return false
    }

    private fun block() {
        preferencesEditor.putLong(BLOCK_TIME, System.currentTimeMillis())
        preferencesEditor.apply()
    }

    override fun onPause() {
        super.onPause()
        preferencesEditor.putInt(ATTEMPT_COUNT, attemptPassword)
        preferencesEditor.apply()
    }
}