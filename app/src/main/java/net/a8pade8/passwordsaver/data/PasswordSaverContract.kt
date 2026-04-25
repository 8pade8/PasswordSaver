package net.a8pade8.passwordsaver.data

import android.provider.BaseColumns

object PasswordSaverContract {

    const val DATA_BASE = "psdb.db"

    object Passwords : BaseColumns {
        const val TABLE_PASSWORDS = "passwords"
        const val _ID = BaseColumns._ID
        const val COLUMN_RESOURCE = "resource"
        const val COLUMN_LOGIN = "login"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_COMMENT = "comment"
        const val COLUMN_FAVORITE = "favorite"
    }
}