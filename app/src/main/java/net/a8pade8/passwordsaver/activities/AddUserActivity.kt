package net.a8pade8.passwordsaver.activities

import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

import net.a8pade8.passwordsaver.R
import net.a8pade8.passwordsaver.R.string.*
import net.a8pade8.passwordsaver.databinding.ActivityAddUserBinding
import net.a8pade8.passwordsaver.security.Security
import net.a8pade8.passwordsaver.uilib.middleToastLong


class AddUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddUserBinding
    private lateinit var security: Security

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_user)
        security = Security.getInstance(this)
    }

    fun addNewUser(view: View?) {
        if (passwordChecked()) {
            security.password = binding.password.toString()
            middleToastLong(this, getString(addingUserSuccessfully))
            finish()
        }
    }

    private fun isPasswordsEquals(pass1: String?, pass2: String?): Boolean {
        if (pass1 != pass2) {
            middleToastLong(this, getString(passwordsNotEquals))
            return false
        }
        return true
    }

    private fun isEasyPassword(): Boolean {
        return if (binding.password in arrayOf("123qwe", "qwe123", "q12345", "12345q", "qwert1", "1qwert", "qwert0",
                        "0qwert", "12qwer", "qwer12")) {
            middleToastLong(this, getString(easyPassword))
            true
        } else false
    }

    private fun passwordChecked(): Boolean {
        return (isPasswordSixSymbols()
                && isPasswordCombined()
                && isPasswordsEquals(binding.password, binding.passwordReplay)
                && !isEasyPassword())
    }

    private fun isPasswordSixSymbols(): Boolean {
        return if (binding.password!!.length >= 6) {
            true
        } else {
            middleToastLong(this, getString(demandForPasswordLength))
            false
        }
    }

    private fun isPasswordCombined(): Boolean {
        var letter = 0
        var digit = 0
        for (i in 0 until binding.password!!.length - 1) {
            if (Character.isLetter(binding.password!!.get(i))) letter++
            if (Character.isDigit(binding.password!!.get(i))) digit++
        }
        return if (letter > 0 && digit > 0) true else {
            middleToastLong(this, getString(demandForPasswordCharacters))
            false
        }
    }
}