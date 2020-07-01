package net.a8pade8.passwordsaver

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
import net.a8pade8.passwordsaver.a8pade8Lib1.Messages
import net.a8pade8.passwordsaver.data.*
import net.a8pade8.passwordsaver.databinding.ActivityEditRecordBinding


class EditRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditRecordBinding
    private val TYPE_AUTO_COMPLETE_EMAIL = 65569

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_record)
        val record: Record = try {
            getRecordFromPasswords(intent.getLongExtra("id", 0))
        } catch (e: IdIsNotExistException) {
            Messages.MiddleToastShort(this, "Ошибка, запись не найдена")
            finish()
            return
        }
        binding.record = record
        binding.passwordRetry = record.password
    }

    fun onReady(view: View) {
        if (binding.record?.password != binding.passwordRetry) {
            Messages.MiddleToastLong(this, "Пароли не совпадают")
            return
        }
        try {
            updateRecordInPasswords(binding.record!!)
            Messages.MiddleToastLong(this, "Запись успешно обновлена")
            finish()
            startActivity(Intent(this, ResourceViewActivity::class.java).putExtra("id", binding.record?.id))
        } catch (e: IdIsNotExistException) {
            Messages.MiddleToastLong(this, "")
            e.printStackTrace()
        } catch (e: ResourceLoginRepeatException) {
            Messages.MiddleToastLong(this, "")
            e.printStackTrace()
        }
    }

    fun onSwitchSite(view: View) {
        if (toggleButtonSite.isChecked) {
            editTextLogin.inputType = TYPE_CLASS_TEXT
        } else {
            editTextLogin.inputType = TYPE_TEXT_VARIATION_URI
        }
    }

    fun onSwitchEmail(view: View) {
        if (toggleButtonEmail.isChecked) {
            editTextLogin.inputType = TYPE_CLASS_TEXT
        } else {
            editTextLogin.inputType = TYPE_AUTO_COMPLETE_EMAIL
        }
    }

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