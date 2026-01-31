package net.a8pade8.passwordsaver.util

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.a8pade8.passwordsaver.R
import net.a8pade8.passwordsaver.data.Record
import net.a8pade8.passwordsaver.data.importRecords
import net.a8pade8.passwordsaver.uiutil.middleToastLong
import java.io.*

fun exportPasswordsToFile(folderName: String, records: List<Record>, context: AppCompatActivity) {
    try {
        val file = File(folderName, "passwords.json")
        file.createNewFile()
        val writer = FileOutputStream(file)
        writer.write(Json.encodeToString(records).toByteArray())
        writer.close()
        middleToastLong(context, context.getString(R.string.passwordsSavedToFile))
    } catch (e: FileNotFoundException) {
        middleToastLong(context, context.getString(R.string.fileNotFound))
    } catch (e: IOException) {
        middleToastLong(context, context.getString(R.string.folderNotExist))
    } catch (e: Exception) {
        middleToastLong(context, context.getString(R.string.exportToFileError))
    }
}

fun importPasswordsFromFile(fileName: String, context: AppCompatActivity) {
    try {
        val reader = FileInputStream(fileName)
        val records: List<Record> = Json.decodeFromString(reader.readBytes().decodeToString())
        reader.close()
        importRecords(records, context)
    } catch (e: FileNotFoundException) {
        middleToastLong(context, context.getString(R.string.fileNotFound))
    } catch (e: IOException) {
        middleToastLong(context, context.getString(R.string.folderNotExist))
    } catch (e: Exception) {
        middleToastLong(context, context.getString(R.string.ErrorImportingPasswordsFromFile))
    }
}



fun verifyStoragePermissions(context: AppCompatActivity): Boolean {
    val oldPermissions = arrayOf<String>(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    val newPermissions = arrayOf<String>(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.READ_MEDIA_AUDIO
    )
    val allOldPermissionNotPresent =
        !oldPermissions.map { ActivityCompat.checkSelfPermission(context, it) }
            .all { it == PackageManager.PERMISSION_GRANTED }
    val allNewPermissionNotPresent =
        !newPermissions.map { ActivityCompat.checkSelfPermission(context, it) }
            .all { it == PackageManager.PERMISSION_GRANTED }
    if (allOldPermissionNotPresent && allNewPermissionNotPresent) {
        ActivityCompat.requestPermissions(context, newPermissions, 1)
        ActivityCompat.requestPermissions(context, oldPermissions, 1)
        return false
    }
    return true
}

fun exportPasswordsToTxtFile(
    folderName: String,
    records: List<Record>,
    context: AppCompatActivity
) {
    try {
        val file = File(folderName, "passwords.txt")
        file.createNewFile()
        val fileWriter = FileWriter(file)
        records.sortedBy { it.resourceName }
        records.forEach {
            fileWriter.write(
                "${it.resourceName} | ${it.login} | ${it.password} | " +
                        "${it.comment} | ${it.favorite} \n"
            )
        }
        fileWriter.close()
        middleToastLong(context, context.getString(R.string.passwordsSavedToFile))
    } catch (e: FileNotFoundException) {
        middleToastLong(context, context.getString(R.string.fileNotFound))
    } catch (e: IOException) {
        middleToastLong(context, context.getString(R.string.folderNotExist))
    } catch (e: Exception) {
        middleToastLong(context, context.getString(R.string.exportToFileError))
    }
}
