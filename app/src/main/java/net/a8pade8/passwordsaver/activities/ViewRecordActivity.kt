package net.a8pade8.passwordsaver.activities

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import net.a8pade8.passwordsaver.R
import net.a8pade8.passwordsaver.R.string.copiedToClipboard
import net.a8pade8.passwordsaver.R.string.siteNotFound
import net.a8pade8.passwordsaver.data.*
import net.a8pade8.passwordsaver.databinding.ActivityResourceViewBinding
import net.a8pade8.passwordsaver.uiutil.middleToastLong
import net.a8pade8.passwordsaver.util.finishAndOpenActivity

class ViewRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResourceViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_resource_view)
        binding.record = try {
            getRecordFromPasswords(intent.getLongExtra("id", 0))
        } catch (e: IdIsNotExistException) {
            middleToastLong(this, getString(R.string.recordIsNotExist))
            finish()
            return
        }
        binding.favoriteToggleButton.setIconEnabled(binding.record!!.favorite)
        fadePassword()
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.mainToolbar)
    }

    private fun showPassword() {
        binding.password = binding.record!!.password
    }

    private fun fadePassword() {
        binding.password = getString(R.string.passwordMask)
    }

    @Suppress("UNUSED_PARAMETER")
    fun switchFadePassword(view: View?) {
        binding.passwordToggleButton.switchState(true)
        if (binding.passwordToggleButton.isIconEnabled) {
            showPassword()
        } else {
            fadePassword()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun delete(view: View?) {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.warning))
                .setMessage(getString(R.string.approveDeleteRecord))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.delete)) { _, _ -> deleteRecord() }
                .setNegativeButton(getString(R.string.cancel)) { dialogInterface: DialogInterface, _ -> dialogInterface.cancel() }
                .create()
                .show()
    }

    private fun deleteRecord() {
        try {
            deleteRecordFromPasswords(binding.record!!.id)
            middleToastLong(this, getString(R.string.recordDeleted))
            finish()
        } catch (e: IdIsNotExistException) {
            middleToastLong(this, getString(R.string.recordDeleteFailed))
            e.printStackTrace()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun edit(view: View?) {
        finishAndOpenActivity(EditRecordActivity::class.java, hashMapOf("id" to binding.record!!.id))
    }

    @Suppress("UNUSED_PARAMETER")
    fun copyInBuffer(view: View?) {
        (getSystemService(CLIPBOARD_SERVICE) as ClipboardManager)
                .setPrimaryClip(ClipData.newPlainText("password", binding.record?.password))
        middleToastLong(this, getString(copiedToClipboard))
    }

    @Suppress("UNUSED_PARAMETER")
    fun goToResource(view: View?) {
        var address = binding.record!!.resourceName
        if (!address.startsWith("http")) {
            address = "http://$address"
        }
        if (Patterns.WEB_URL.matcher(address).matches()) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(address))
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        } else {
            middleToastLong(this, getString(siteNotFound))
        }
    }

    @Throws(ResourceLoginRepeatException::class, IdIsNotExistException::class)
    @Suppress("UNUSED_PARAMETER")
    fun switchFavorite(view: View?) {
        try {
            binding.favoriteToggleButton.switchState(true)
            binding.record?.favorite = binding.favoriteToggleButton.isIconEnabled
            updateRecordInPasswords(binding.record!!)
        } catch (e: IdIsNotExistException) {
            middleToastLong(this, getString(R.string.recordIsNotExist))
            e.printStackTrace()
        }
    }
}