package net.a8pade8.passwordsaver.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.*
import net.a8pade8.passwordsaver.security.Security
import net.sqlcipher.database.SQLiteDatabase

lateinit var dataBase: SQLiteDatabase

fun loading(context: Context) {
    dataBase = PSDBHelperCrypto(context).getDataBase((Security.getInstance(context).cryptoKey))
}

@Throws(EmptyDataException::class, ResourceLoginRepeatException::class)
fun addRecordToPasswords(resourceName: String, login: String, password: String, comment: String = "", favorite: Boolean = false): Long {
    if (resourceName.isBlank() || login.isBlank() || password.isBlank()) throw EmptyDataException()
    if (isContainResourceLoginInPasswords(resourceName, login)) throw ResourceLoginRepeatException()
    val cv = ContentValues()
    cv.let {
        it.put(COLUMN_RESOURCE, resourceName)
        it.put(COLUMN_LOGIN, login)
        it.put(COLUMN_PASSWORD, password)
        it.put(COLUMN_COMMENT, comment)
        it.put(COLUMN_FAVORITE, if (favorite) 1 else 0)
    }
    return dataBase.insert(TABLE_PASSWORDS, null, cv)
}

fun isContainResourceLoginInPasswords(resourceName: String, login: String): Boolean {
    return dataBase.query(
            TABLE_PASSWORDS,
            null,
            "$COLUMN_RESOURCE=? and $COLUMN_LOGIN=?",
            arrayOf(resourceName, login), null, null, null).count > 0
}

private fun isContainResourceLoginAnotherIdInPasswords(resourceName: String, login: String, id: Long): Boolean {
    return dataBase.query(
            TABLE_PASSWORDS,
            null,
            "$COLUMN_RESOURCE=? and $COLUMN_LOGIN=? and $_ID<>?",
            arrayOf(resourceName, login, id.toString()), null, null, null).count > 0
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
    return mapCursorToRecordsList(dataBase.query(
            TABLE_PASSWORDS,
            null, null, null, null, null, COLUMN_RESOURCE))
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

@Throws(IdIsNotExistException::class, ResourceLoginRepeatException::class)
fun updateRecordInPasswords(record: Record) {
    if (isContainResourceLoginAnotherIdInPasswords(record.resourceName, record.login, record.id)) throw ResourceLoginRepeatException()
    val cv = ContentValues()
    cv.let {
        it.put(COLUMN_LOGIN, record.login)
        it.put(COLUMN_PASSWORD, record.password)
        it.put(COLUMN_RESOURCE, record.resourceName)
        it.put(COLUMN_COMMENT, record.comment)
        it.put(COLUMN_FAVORITE, if (record.favorite) 1 else 0)
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
                            cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_COMMENT)),
                            cursor.getInt(cursor.getColumnIndex(COLUMN_FAVORITE)) == 1))
        } while (cursor.moveToNext())
    }
    cursor.close()
    return recordsList
}

class EmptyDataException : Exception()

class IdIsNotExistException : Exception()

class ResourceLoginRepeatException : Exception()
