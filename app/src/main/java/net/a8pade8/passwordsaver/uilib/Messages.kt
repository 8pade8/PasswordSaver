package net.a8pade8.passwordsaver.uilib

import android.content.Context
import android.view.Gravity
import android.widget.Toast

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