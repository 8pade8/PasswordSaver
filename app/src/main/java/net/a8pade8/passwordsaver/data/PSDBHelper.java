package net.a8pade8.passwordsaver.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import net.a8pade8.passwordsaver.data.PasswordSaverContract.Users;
import net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords;

/**
 * Created by stanislav on 07.12.16.
 */

public class PSDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "psdb.db";
    private static final int DATABASE_VERSION = 1;


    PSDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String SQL_CREATE_USERS_TABLE = "CREATE TABLE " + Users.TABLE_NAME + " ("
                + Users._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Users.COLUMN_PASSWORD + " INTEGER NOT NULL);";
        String SQL_CREATE_PASSWORDS_TABLE = "CREATE TABLE " + Passwords.TABLE_NAME + " ("
                + Passwords._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Passwords.COLUMN_RESOURCE + " TEXT NOT NULL, "
                + Passwords.COLUMN_LOGIN + " TEXT NOT NULL, "
                + Passwords.COLUMN_PASSWORD + " TEXT NOT NULL, "
                + Passwords.COLUMN_USER + " INTEGER NOT NULL);";
        // CREATE TABLE users (_ID INTEGER PRIMIRY KEY AUTOINCREMENT, password TEXT NOT NULL);
        sqLiteDatabase.execSQL(SQL_CREATE_USERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PASSWORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
