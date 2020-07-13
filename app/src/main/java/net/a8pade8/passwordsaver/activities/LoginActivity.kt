package net.a8pade8.passwordsaver.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import net.a8pade8.passwordsaver.R
import net.a8pade8.passwordsaver.data.loading
import net.a8pade8.passwordsaver.security.Security
import net.a8pade8.passwordsaver.uilib.middleToastLong
import net.a8pade8.passwordsaver.util.generateTestData

class LoginActivity : AppCompatActivity() {
    private val generateTestData = false // Генерировать тестовые данные
    private val crypto = true //Определеят шифровать ли данные, задел на будущее
    private lateinit var security: Security
    private lateinit var passwordIn: EditText
    private var attemptPassword = 3
    private lateinit var preferences: SharedPreferences
    private lateinit var preferencesEditor: SharedPreferences.Editor
    private val BLOCK_TIME = "blockTime"
    private val ATTEMPT_COUNT = "attempts"

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        security = Security.getInstance(this)
        setContentView(R.layout.activity_login)
        loading(this, crypto)
        generateTestData(this, generateTestData) //Генерация тестовых данных
        if (security.password.isEmpty()) {
            openAddUserActivity(null)
        }
        passwordIn = findViewById(R.id.password)
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
    }

    @Suppress("UNUSED_PARAMETER")
    private fun openAddUserActivity(view: View?) {
        startActivity(Intent(this, AddUserActivity::class.java))
    }

    @Suppress("UNUSED_PARAMETER")
    fun openMainActivity(view: View?) {
        if (isAttemptExist() && isPasswordExist()) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun isPasswordExist(): Boolean {
        if (security.password != passwordIn.text.toString()) {
            middleToastLong(this, getString(R.string.attemptsWarning) + --attemptPassword)
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
            middleToastLong(this, getString(R.string.noAttempts))
            passwordIn.setText("")
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