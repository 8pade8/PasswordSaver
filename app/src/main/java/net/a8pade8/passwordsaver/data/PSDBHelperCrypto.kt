package net.a8pade8.passwordsaver.data

import android.content.Context
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteOpenHelper

class PSDBHelperCrypto(context: Context) : SQLiteOpenHelper(context, PasswordSaverContract.DATA_BASE, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
    }

    init {
        SQLiteDatabase.loadLibs(context)
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val SQL_CREATE_PASSWORDS_TABLE = "CREATE TABLE ${PasswordSaverContract.Passwords.TABLE_PASSWORDS} (" +
                "${PasswordSaverContract.Passwords._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${PasswordSaverContract.Passwords.COLUMN_RESOURCE} TEXT NOT NULL, " +
                "${PasswordSaverContract.Passwords.COLUMN_LOGIN} TEXT NOT NULL, " +
                "${PasswordSaverContract.Passwords.COLUMN_PASSWORD} TEXT NOT NULL, " +
                "${PasswordSaverContract.Passwords.COLUMN_FAVORITE} INTEGER NOT NULL, " +
                "${PasswordSaverContract.Passwords.COLUMN_COMMENT} TEXT) ;"
        val SQL_CREATE_PASSWORD_TABLE_UNIQUE = " CREATE UNIQUE INDEX passwordUnIndex \n" +
                " ON ${PasswordSaverContract.Passwords.TABLE_PASSWORDS} (${PasswordSaverContract.Passwords.COLUMN_LOGIN}," +
                "${PasswordSaverContract.Passwords.COLUMN_RESOURCE});"

        sqLiteDatabase.execSQL(SQL_CREATE_PASSWORDS_TABLE)
        sqLiteDatabase.execSQL(SQL_CREATE_PASSWORD_TABLE_UNIQUE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Not implemented
    }

    fun getDataBase(password: String): SQLiteDatabase {
        return getWritableDatabase(password)
    }
}