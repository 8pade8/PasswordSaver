package net.a8pade8.passwordsaver.util

import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.a8pade8.passwordsaver.R
import net.a8pade8.passwordsaver.data.Record
import net.a8pade8.passwordsaver.data.importRecords
import net.a8pade8.passwordsaver.uiutil.middleToastLong
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


fun exportPasswordsToFile(
    outputStream: OutputStream,
    records: List<Record>,
    context: AppCompatActivity
) {
    try {
        outputStream.write(Json.encodeToString(records).toByteArray())
        outputStream.close()
        middleToastLong(context, context.getString(R.string.passwordsSavedToFile))
    } catch (e: FileNotFoundException) {
        middleToastLong(context, context.getString(R.string.fileNotFound))
    } catch (e: IOException) {
        middleToastLong(context, context.getString(R.string.folderNotExist))
    } catch (e: Exception) {
        middleToastLong(context, context.getString(R.string.exportToFileError))
    }
}

fun importPasswordsFromFile(fileName: InputStream, context: AppCompatActivity) {
    try {
        val records: List<Record> = Json.decodeFromString(fileName.readBytes().decodeToString())
        fileName.close()
        importRecords(records, context)
    } catch (e: FileNotFoundException) {
        middleToastLong(context, context.getString(R.string.fileNotFound))
    } catch (e: IOException) {
        middleToastLong(context, context.getString(R.string.folderNotExist))
    } catch (e: Exception) {
        middleToastLong(context, context.getString(R.string.ErrorImportingPasswordsFromFile))
    }
}

fun exportPasswordsToTxtFile(
    outputStream: OutputStream,
    records: List<Record>,
    context: AppCompatActivity
) {
    try {
        val sb: StringBuilder = StringBuilder()
        records.sortedBy { it.resourceName }
        records.forEach {
            sb.appendLine("${it.resourceName} | ${it.login} | ${it.password} | " + "${it.comment} | ${it.favorite}")
        }
        outputStream.write(sb.toString().toByteArray())
        outputStream.close()
        middleToastLong(context, context.getString(R.string.passwordsSavedToFile))
    } catch (e: FileNotFoundException) {
        middleToastLong(context, context.getString(R.string.fileNotFound))
    } catch (e: IOException) {
        middleToastLong(context, context.getString(R.string.folderNotExist))
    } catch (e: Exception) {
        middleToastLong(context, context.getString(R.string.exportToFileError))
    }
}
