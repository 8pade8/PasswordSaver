package net.a8pade8.passwordsaver.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.*

lateinit var dataBase: DataBaseAdapter

fun loading(context: Context, withCrypto: Boolean = false) {
    dataBase = if (withCrypto) {
        val dataBaseHelper = PSDBHelperCrypto(context)
        dataBaseHelper.getDataBase((User.getInstance(context).cryptoKey)) //нужке другой метод возвращающий интерфейс
    } else {
        val dataBaseHelper = PSDBHelper(context)
        dataBaseHelper.getDataBase()
    }
}

@Throws(EmptyDataException::class)
fun addRecordToPasswords(resourceName: String, login: String, password: String): Long {
    if (resourceName.isBlank() || login.isBlank() || password.isBlank()) throw EmptyDataException()
    val cv = ContentValues()
    cv.let {
        it.put(COLUMN_RESOURCE, resourceName)
        it.put(COLUMN_LOGIN, login)
        it.put(COLUMN_PASSWORD, password)
    }
    return dataBase.insert(TABLE_PASSWORDS, null, cv)
}

fun isRecordExistInPasswords(id: Long): Boolean {
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
fun getRecordFromPasswords(id: Long): Record {
    val cursor = dataBase.query(
            TABLE_PASSWORDS,
            null,
            "$_ID=?",
            arrayOf("$id"), null, null, null)
    if (cursor.count == 0) throw IdIsNotExistException()
    return mapCursorToRecordsList(cursor).iterator().next()
}

@Throws(IdIsNotExistException::class)
fun deleteRecordFromPasswords(id: Long) {
    val result = dataBase.delete(
            TABLE_PASSWORDS,
            "$_ID=?",
            arrayOf("$id"))
    if (result != 1) throw IdIsNotExistException()
}

@Throws(IdIsNotExistException::class)
fun updateRecordInPasswords(record: Record) {
    val cv = ContentValues()
    cv.let {
        it.put(COLUMN_LOGIN, record.login)
        it.put(COLUMN_PASSWORD, record.password)
        it.put(COLUMN_RESOURCE, record.resourceName)
    }
    val result = dataBase.update(TABLE_PASSWORDS, cv, "$_ID=${record.id}", null)
    if (result != 1) throw  IdIsNotExistException()
}

private fun mapCursorToRecordsList(cursor: Cursor): List<Record> {
    val recordsList = mutableListOf<Record>()
    if (cursor.moveToFirst()) {
        do {
            recordsList.add(
                    Record(
                            cursor.getLong(cursor.getColumnIndex(_ID)),
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