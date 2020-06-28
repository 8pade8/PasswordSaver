package net.a8pade8.passwordsaver.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.*

lateinit var dataBase: SQLiteDatabase

fun loading(context: Context) {
    val dataBaseHelper = PSDBHelper(context)
    dataBase = dataBaseHelper.readableDatabase
}

@Throws(EmptyDataException::class)
fun addRecordToPasswords(resourceName: String, login: String, password: String) {
    if (resourceName.isBlank() || login.isBlank() || password.isBlank()) throw EmptyDataException()
    ContentValues().let {
        it.put(COLUMN_RESOURCE, resourceName)
        it.put(COLUMN_LOGIN, login)
        it.put(COLUMN_PASSWORD, password)
        dataBase.insert(TABLE_PASSWORDS, null, it)
    }
}

fun isRecordExistInPasswords(id: Int): Boolean {
    val cursor = dataBase.query(
            TABLE_PASSWORDS,
            null,
            "$_ID=?",
            arrayOf("$id"), null, null, null)
    val countRows = cursor.count
    cursor.close()
    return countRows != 0
}

fun isContainResourceInPasswords(resource: String): Boolean {
    val cursor = dataBase.query(
            TABLE_PASSWORDS,
            null,
            "$COLUMN_RESOURCE=?",
            arrayOf(resource), null, null, null)
    val countRows = cursor.count
    cursor.close()
    return countRows != 0
}

fun getAllRecordsFromPasswords(): List<Record> {
    val cursor = dataBase.query(
            TABLE_PASSWORDS,
            null, null, null, null, null, null)
    return mapCursorToRecordsList(cursor)
}

@Throws(IdIsNotExistException::class)
fun getRecordFromPasswords(id: Int): Record {
    val cursor = dataBase.query(
            TABLE_PASSWORDS,
            null,
            "$_ID=?",
            arrayOf("$id"), null, null, null)
    if (cursor.count == 0) throw IdIsNotExistException()
    return mapCursorToRecordsList(cursor).iterator().next()
}

@Throws(IdIsNotExistException::class)
fun deleteRecordFromPasswords(id: Int) {
    val result = dataBase.delete(
            TABLE_PASSWORDS,
            "$_ID=?",
            arrayOf("$id"))
    if (result != 1) throw IdIsNotExistException()
}

private fun mapCursorToRecordsList(cursor: Cursor): List<Record> {
    val recordsList = mutableListOf<Record>()
    if (cursor.moveToFirst()) {
        do {
            recordsList.add(
                    Record(
                            cursor.getInt(cursor.getColumnIndex(_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_RESOURCE)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_LOGIN)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
                    ))
        } while (cursor.moveToNext())
    }
    cursor.close()
    return recordsList
}

class EmptyDataException : Exception()

class IdIsNotExistException : Exception()