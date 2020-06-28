package net.a8pade8.passwordsaver.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.COLUMN_LOGIN;
import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.COLUMN_PASSWORD;
import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.COLUMN_RESOURCE;
import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.TABLE_NAME;
import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords._ID;


/**
 * Created by stanislav on 07.12.16.
 */

public final class db {

    public static SQLiteDatabase DB;

    public static void loading(Context context) {
        PSDBHelper DataBaseHelper = new PSDBHelper(context);
        DB = DataBaseHelper.getReadableDatabase();
    }

    public static void addRecordToPasswords(String resourceName, String login, String password) throws EmptyDataException {
        if (resourceName.isEmpty() || login.isEmpty() || password.isEmpty())
            throw new EmptyDataException();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RESOURCE, resourceName);
        cv.put(COLUMN_LOGIN, login);
        cv.put(COLUMN_PASSWORD, password);
        DB.insert(TABLE_NAME, null, cv);
    }

    public static boolean isRecordExistInPasswords(int id) {
        Cursor cursor = DB.query(PasswordSaverContract.Passwords.TABLE_NAME, null, _ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        int countRows = cursor.getCount();
        cursor.close();
        return countRows != 0;
    }

    public static boolean isContainResourceInPasswords(String resource) {
        Cursor cursor = DB.query(PasswordSaverContract.Passwords.TABLE_NAME, null, COLUMN_RESOURCE + "=?",
                new String[]{resource}, null, null, null);
        int countRows = cursor.getCount();
        cursor.close();
        return countRows != 0;
    }

    public static List<Record> getAllRecordsFromPasswords() {
        Cursor cursor = DB.query(TABLE_NAME, null,
                null, null, null, null, null);
        return mapCursorToRecordsList(cursor);
    }

    public static Record getRecordFromPasswords(int id) throws IdIsNotExistException {
        Cursor cursor = DB.query(TABLE_NAME
                , null
                , _ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.getCount() == 0) throw new IdIsNotExistException();
        return mapCursorToRecordsList(cursor).iterator().next();
    }

    public static void deleteRecordFromPasswords(int id) throws IdIsNotExistException {
        int result = DB.delete(TABLE_NAME,
                _ID + "=?",
                new String[]{String.valueOf(id)});
        if (result != 1) throw new IdIsNotExistException();
    }

    private static List<Record> mapCursorToRecordsList(Cursor cursor) {
        List<Record> recordsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                recordsList.add(
                        new Record(
                                cursor.getInt(cursor.getColumnIndex(_ID)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_RESOURCE)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_LOGIN)),
                                cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
                        ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recordsList;
    }

    public static class EmptyDataException extends Exception {
    }

    public static class IdIsNotExistException extends Exception {
    }
}

