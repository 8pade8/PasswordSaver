package net.a8pade8.passwordsaver.data;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.a8pade8.passwordsaver.LoginActivity;
import net.a8pade8.passwordsaver.data.PasswordSaverContract.Users;
import net.a8pade8.passwordsaver.data.PasswordSaverContract.Passwords;

import static net.a8pade8.passwordsaver.LoginActivity.CURRENT_USER;


/**
 * Created by stanislav on 07.12.16.
 */

public final class db {

    public static SQLiteDatabase DB;

    public static void loading(Context context){

        PSDBHelper DataBaseHelper = new PSDBHelper(context);
        DB = DataBaseHelper.getReadableDatabase();
    }

    public static boolean isContainPasswordInUsers (String password){
        if (!password.equals("")){
        Cursor cursor = DB.query(Users.TABLE_NAME,null,Users.COLUMN_PASSWORD+"="+
                Integer.parseInt(password),null,null, null,null);
        int countRows = cursor.getCount();
        cursor.close();
        return countRows!=0;}
        else {
        return false;}
    }

    public static void insertPasswordToUsers(String password){
        ContentValues cv = new ContentValues();
        cv.put(Users.COLUMN_PASSWORD, Integer.parseInt(password));
        DB.insert(Users.TABLE_NAME,null,cv);}

    public static void addRecordToPasswords(String resource, String login, String password){
        ContentValues cv = new ContentValues();
        cv.put(Passwords.COLUMN_RESOURCE, resource);
        cv.put(Passwords.COLUMN_LOGIN, login);
        cv.put(Passwords.COLUMN_PASSWORD,password);
        cv.put(Passwords.COLUMN_USER, CURRENT_USER);
        DB.insert(Passwords.TABLE_NAME, null, cv);
    }

    public static boolean isContainResourceInPasswords(String resource){
        if(!resource.equals("")){
            Cursor cursor = DB.query(Passwords.TABLE_NAME,null,Passwords.COLUMN_RESOURCE+"=?" +
                    " AND "+Passwords.COLUMN_USER+"="+LoginActivity.CURRENT_USER,
                    new String[]{resource},null,null,null);
            int countRows = cursor.getCount();
            cursor.close();
            return countRows!=0;
        }
            else {
                return false;
        }
    }

    public static Cursor getAllResourcesOfPasswords()
    {
        return DB.query(Passwords.TABLE_NAME,new String[]{Passwords.COLUMN_RESOURCE},
                Passwords.COLUMN_USER+"="+LoginActivity.CURRENT_USER,
                null,null,null,null);
    }
    public static Cursor getResourceOfPasswords(String resource)
    {
        return DB.query(Passwords.TABLE_NAME
                , new String[]{
                Passwords.COLUMN_PASSWORD,Passwords.COLUMN_LOGIN}
                , Passwords.COLUMN_USER+"="+LoginActivity.CURRENT_USER +" AND "+ Passwords.COLUMN_RESOURCE+"=?",
                new String[]{resource},null,null,null);
    }
}
