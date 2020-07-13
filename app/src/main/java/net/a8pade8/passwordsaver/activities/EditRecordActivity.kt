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
import net.a8pade8.passwordsaver.R

import net.a8pade8.passwordsaver.R.layout.*
import net.a8pade8.passwordsaver.R.string.*
import net.a8pade8.passwordsaver.data.*
import net.a8pade8.passwordsaver.databinding.ActivityEditRecordBinding
import net.a8pade8.passwordsaver.uilib.middleToastLong


class EditRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditRecordBinding
    private val TYPE_AUTO_COMPLETE_EMAIL = 65569

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, activity_edit_record)
        val record: Record = try {
            getRecordFromPasswords(intent.getLongExtra("id", 0))
        } catch (e: IdIsNotExistException) {
            middleToastLong(this, getString(recordIsNotExist))
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
            middleToastLong(this, getString(passwordsNotEquals))
            return
        }
        try {
            updateRecordInPasswords(binding.record!!)
            middleToastLong(this, getString(recordUpdateSucsessfully))
            finish()
            startActivity(Intent(this, ViewRecordActivity::class.java).putExtra("id", binding.record?.id))
        } catch (e: IdIsNotExistException) {
            middleToastLong(this, getString(recordIsNotExist))
            e.printStackTrace()
        } catch (e: ResourceLoginRepeatException) {
            middleToastLong(this, getString(repeatLoginResource))
            e.printStackTrace()
        }
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
}