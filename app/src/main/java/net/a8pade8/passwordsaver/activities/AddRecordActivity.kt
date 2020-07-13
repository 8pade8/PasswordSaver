package net.a8pade8.passwordsaver.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_URI
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil

import kotlinx.android.synthetic.main.activity_add_record.*

import net.a8pade8.passwordsaver.R
import net.a8pade8.passwordsaver.R.string.*
import net.a8pade8.passwordsaver.data.*
import net.a8pade8.passwordsaver.databinding.ActivityAddRecordBinding
import net.a8pade8.passwordsaver.uilib.middleToastLong
import net.a8pade8.passwordsaver.util.generateAlthaNumericString

class AddRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRecordBinding
    private val TYPE_AUTO_COMPLETE_EMAIL = 65569
    private var record = Record(0, "", "", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_record)
        binding.record = record
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.mainToolbar)
    }

    @Suppress("UNUSED_PARAMETER")
    fun onSwitchSite(view: View) {
        if (toggleButtonSite.isChecked) {
            editTextLogin.inputType = TYPE_CLASS_TEXT
        } else {
            editTextLogin.inputType = TYPE_TEXT_VARIATION_URI
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onSwitchEmail(view: View) {
        if (toggleButtonEmail.isChecked) {
            editTextLogin.inputType = TYPE_CLASS_TEXT
        } else {
            editTextLogin.inputType = TYPE_AUTO_COMPLETE_EMAIL
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onSwitchFade(view: View) {
        if (!toggleButtonFade.isChecked) {
            editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            editTextPasswordReplay.transformationMethod = PasswordTransformationMethod.getInstance()
        } else {
            editTextPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            editTextPasswordReplay.transformationMethod = HideReturnsTransformationMethod.getInstance()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onReady(view: View) {
        if (isValuesChecked() && isPasswordsEquals()) {
            if (!isResourceExist()) {
                addRecord()
            } else {
                messageDoubleResource()
            }
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

    private fun isPasswordsEquals(): Boolean {
        return if (record.password != binding.passwordRetry) {
            middleToastLong(this, getString(passwordsNotEquals))
            false
        } else {
            true
        }
    }

    private fun isResourceExist(): Boolean {
        return isContainResourceInPasswords(record.resourceName)
    }

    private fun messageDoubleResource() {
        AlertDialog.Builder(this)
                .setTitle(getString(warning))
                .setMessage(getString(repeatResource))
                .setCancelable(false)
                .setPositiveButton(getString(add)) { dialogInterface: DialogInterface?, i: Int -> addRecord() }
                .setNegativeButton(getString(cancel)) { dialogInterface: DialogInterface, i: Int -> dialogInterface.cancel() }
                .create()
                .show()
    }

    private fun isValuesChecked(): Boolean {
        return (isResourceNotEmpty()
                && isLoginNotEmpty()
                && isPasswordNotEmpty())
    }

    private fun isResourceNotEmpty(): Boolean {
        return if (record.resourceName.isEmpty()) {
            middleToastLong(this, getString(emptyResourceName))
            false
        } else {
            true
        }
    }

    private fun isLoginNotEmpty(): Boolean {
        return if (record.login.isEmpty()) {
            middleToastLong(this, getString(emptyLogin))
            false
        } else {
            true
        }
    }

    private fun isPasswordNotEmpty(): Boolean {
        if (record.password.isEmpty()) {
            middleToastLong(this, getString(emptyPassword))
            return false
        }
        return true
    }

    private fun addRecord() {
        try {
            addRecordToPasswords(record.resourceName, record.login, record.password)
            middleToastLong(this, getString(recordAddedSucsessfully))
            finish()
        } catch (e: EmptyDataException) {
            middleToastLong(this, getString(dataError))
            e.printStackTrace()
        } catch (e: ResourceLoginRepeatException) {
            middleToastLong(this, getString(repeatLoginResource))
            e.printStackTrace()
        }
    }
}