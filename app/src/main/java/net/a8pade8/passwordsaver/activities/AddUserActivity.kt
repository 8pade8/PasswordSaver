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
import net.a8pade8.passwordsaver.util.isAlthaNumeric


class AddUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddUserBinding
    private lateinit var security: Security

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_user)
        security = Security.getInstance(this)
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.mainToolbar)
    }

    @Suppress("UNUSED_PARAMETER")
    fun addNewUser(view: View) {
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

    private fun passwordChecked(): Boolean {
        return (isPasswordSixSymbols()
                && isPasswordCombined()
                && isPasswordsEquals(binding.password, binding.passwordReplay))
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
        return if (isAlthaNumeric(binding.password.toString())) {
            true
        } else {
            middleToastLong(this, getString(demandForPasswordCharacters))
            false
        }
    }
}