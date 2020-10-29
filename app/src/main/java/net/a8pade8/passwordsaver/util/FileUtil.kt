package net.a8pade8.passwordsaver.util

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.a8pade8.passwordsaver.R
import net.a8pade8.passwordsaver.data.Record
import net.a8pade8.passwordsaver.uiutil.middleToastLong
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

fun exportPasswordsToFile(folderName: String, records: List<Record>, context:AppCompatActivity) {
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

fun verifyStoragePermissions(context: AppCompatActivity):Boolean {
    val permissions = arrayOf<String>(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    if (permission != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(
                context,
                permissions,
                1
        )
        return false
    }
    return true
}
