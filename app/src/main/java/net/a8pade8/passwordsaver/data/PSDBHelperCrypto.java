package net.a8pade8.passwordsaver.data;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import static net.a8pade8.passwordsaver.data.PasswordSaverContract.DATA_BASE;
import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.COLUMN_COMMENT;
import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.COLUMN_FAVORITE;
import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.COLUMN_LOGIN;
import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.COLUMN_PASSWORD;
import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.COLUMN_RESOURCE;
import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords.TABLE_PASSWORDS;
import static net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords._ID;

public class PSDBHelperCrypto extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;


    PSDBHelperCrypto(Context context) {
        super(context, DATA_BASE, null, DATABASE_VERSION);
        SQLiteDatabase.loadLibs(context);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        String SQL_CREATE_PASSWORDS_TABLE = "CREATE TABLE " + TABLE_PASSWORDS + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_RESOURCE + " TEXT NOT NULL, "
                + COLUMN_LOGIN + " TEXT NOT NULL, "
                + COLUMN_PASSWORD + " TEXT NOT NULL, "
                + COLUMN_FAVORITE + " INTEGER NOT NULL, "
                + COLUMN_COMMENT + " TEXT) ;";
        String SQl_CREATE_PASSWORD_TABLE_UNIQUE = " CREATE UNIQUE INDEX passwordUnIndex \n"
                + " ON " + TABLE_PASSWORDS + " (" + COLUMN_LOGIN + ","
                + COLUMN_RESOURCE + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_PASSWORDS_TABLE);
        sqLiteDatabase.execSQL(SQl_CREATE_PASSWORD_TABLE_UNIQUE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public DataBaseAdapter getDataBase(String password) {
        return new DataBaseAdapter(getWritableDatabase(password));
    }

}
