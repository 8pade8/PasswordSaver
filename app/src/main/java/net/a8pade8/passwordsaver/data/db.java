package net.a8pade8.passwordsaver.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.COLUMN_LOGIN;
import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.COLUMN_PASSWORD;
import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.COLUMN_RESOURCE;
import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.TABLE_NAME;


/**
 * Created by stanislav on 07.12.16.
 */

public final class db {

    public static SQLiteDatabase DB;

    public static void loading(Context context) {
        PSDBHelper DataBaseHelper = new PSDBHelper(context);
        DB = DataBaseHelper.getReadableDatabase();
    }

    public static void addRecordToPasswords(String resource, String login, String password) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RESOURCE, resource);
        cv.put(COLUMN_LOGIN, login);
        cv.put(COLUMN_PASSWORD, password);
        DB.insert(TABLE_NAME, null, cv);
    }

    public static boolean isContainResourceInPasswords(String resource) {
        if (!resource.equals("")) {
            Cursor cursor = DB.query(TABLE_NAME, null, COLUMN_RESOURCE + "=?",
                    new String[]{resource}, null, null, null);
            int countRows = cursor.getCount();
            cursor.close();
            return countRows != 0;
        } else {
            return false;
        }
    }

    public static Cursor getAllResourcesOfPasswords() {
        return DB.query(TABLE_NAME, new String[]{COLUMN_RESOURCE},
                null, null, null, null, null);
    }

    public static Cursor getResourceOfPasswords(String resource) {
        return DB.query(TABLE_NAME
                , new String[]{COLUMN_PASSWORD, COLUMN_LOGIN}
                , COLUMN_RESOURCE + "=?",
                new String[]{resource}, null, null, null);
    }
}
