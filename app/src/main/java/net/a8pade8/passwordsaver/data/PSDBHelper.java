package net.a8pade8.passwordsaver.data;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import static net.a8pade8.passwordsaver.data.PasswordSaverContract.DATA_BASE;
import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.COLUMN_COMMENT;
import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.COLUMN_LOGIN;
import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.COLUMN_PASSWORD;
import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.COLUMN_RESOURCE;
import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.TABLE_PASSWORDS;
import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords._ID;

public class PSDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;


    PSDBHelper(Context context) {
        super(context, DATA_BASE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_PASSWORDS_TABLE = "CREATE TABLE " + TABLE_PASSWORDS + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_RESOURCE + " TEXT NOT NULL, "
                + COLUMN_LOGIN + " TEXT NOT NULL, "
                + COLUMN_PASSWORD + " TEXT NOT NULL, "
                + COLUMN_COMMENT + " TEXT) ;";
        String SQl_CREATE_PASSWORD_TABLE_UNIQUE = " CREATE UNIQUE INDEX passwordUnIndex \n"
                + " ON " + TABLE_PASSWORDS + " (" + COLUMN_LOGIN + ","
                + COLUMN_RESOURCE + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_PASSWORDS_TABLE);
        sqLiteDatabase.execSQL(SQl_CREATE_PASSWORD_TABLE_UNIQUE);
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public DataBaseAdapter getDataBase() {
        return new DataBaseAdapter(getWritableDatabase());
    }
}
