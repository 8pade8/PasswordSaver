package net.a8pade8.passwordsaver.uiutil

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

fun middleToastShort(c: Context, message: String) {
    Toast.makeText(c, message, Toast.LENGTH_SHORT).let {
        it.setGravity(Gravity.CENTER, -10, 0)
        it.show()
    }
}

fun middleToastLong(c: Context, message: String) {
    Toast.makeText(c, message, Toast.LENGTH_LONG).let {
        it.setGravity(Gravity.CENTER, -10, 0)
        it.show()
    }
}

fun AppCompatActivity.showShortSnack(message: String, action: CharSequence? = null, onClickListener: View.OnClickListener? = null) {
    if (action != null && onClickListener != null) {
        Snackbar.make(this.window.decorView.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
                .setAction(action, onClickListener)
                .show()
    } else {
        Snackbar.make(this.window.decorView.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
                .show()
    }
}

fun AppCompatActivity.showLongSnack(message: String, action: CharSequence?, onClickListener: View.OnClickListener?) {
    if (action != null && onClickListener != null) {
        Snackbar.make(this.window.decorView.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .setAction(action, onClickListener)
                .show()
    } else {
        Snackbar.make(this.window.decorView.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .show()
    }
}