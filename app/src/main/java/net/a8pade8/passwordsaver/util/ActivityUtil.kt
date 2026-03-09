package net.a8pade8.passwordsaver.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable

fun AppCompatActivity.openActivity(clazz: Class<*>, extra: HashMap<String, Serializable>? = null, requestCode: Int = 0) {
    val intent = Intent(this, clazz)
    extra?.forEach { intent.putExtra(it.key, it.value) }
    startActivityForResult(intent, requestCode)
}

fun AppCompatActivity.finishAndOpenActivity(clazz: Class<*>, extra: HashMap<String, Serializable>? = null) {
    openActivity(clazz, extra)
    finish()
}