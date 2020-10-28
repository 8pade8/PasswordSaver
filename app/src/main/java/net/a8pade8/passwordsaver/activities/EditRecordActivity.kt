package net.a8pade8.passwordsaver.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_URI
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

import kotlinx.android.synthetic.main.activity_edit_record.*

import net.a8pade8.passwordsaver.R.layout.*
import net.a8pade8.passwordsaver.R.string.*
import net.a8pade8.passwordsaver.data.*
import net.a8pade8.passwordsaver.databinding.ActivityEditRecordBinding
import net.a8pade8.passwordsaver.uiutil.showShortSnack
import net.a8pade8.passwordsaver.util.generateAlthaNumericString


class EditRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditRecordBinding
    private val TYPE_AUTO_COMPLETE_EMAIL = 65569
    private lateinit var record: Record

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, activity_edit_record)
        record = try {
            getRecordFromPasswords(intent.getLongExtra("id", 0))
        } catch (e: IdIsNotExistException) {
            showShortSnack(getString(recordIsNotExist))
            finish()
            return
        }
        binding.record = record
        binding.passwordRetry = record.password
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.mainToolbar)
    }

    @Suppress("UNUSED_PARAMETER")
    fun onReady(view: View) {
        if (binding.record?.password != binding.passwordRetry) {
            showShortSnack(getString(passwordsNotEquals))
            return
        }
        try {
            updateRecordInPasswords(binding.record!!)
            showShortSnack(getString(recordUpdateSucsessfully))
            finish()
            startActivity(Intent(this, ViewRecordActivity::class.java).putExtra("id", binding.record?.id))
        } catch (e: IdIsNotExistException) {
            showShortSnack(getString(recordIsNotExist))
            e.printStackTrace()
        } catch (e: ResourceLoginRepeatException) {
            showShortSnack(getString(repeatLoginResource))
            e.printStackTrace()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onSwitchSite(view: View) {
        resourceToggleButton.switchState(true)
        if (resourceToggleButton.isIconEnabled) {
            loginEditText.inputType = TYPE_TEXT_VARIATION_URI
        } else {
            loginEditText.inputType = TYPE_CLASS_TEXT
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onSwitchEmail(view: View) {
        loginToggleButton.switchState(true)
        if (loginToggleButton.isIconEnabled) {
            loginEditText.inputType = TYPE_AUTO_COMPLETE_EMAIL
        } else {
            loginEditText.inputType = TYPE_CLASS_TEXT
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onSwitchFade(view: View) {
        passwordToggleButton.switchState(true)
        if (!passwordToggleButton.isIconEnabled) {
            passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            repeatPasswordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
        } else {
            passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            repeatPasswordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun generatePassword(view: View) {
        generateAlthaNumericString(6).let {
            record.password = it
            binding.passwordRetry = it
            binding.invalidateAll()
        }
    }
}