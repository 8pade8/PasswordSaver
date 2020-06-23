package net.a8pade8.passwordsaver.data;

import android.provider.BaseColumns;

/**
 * Created by stanislav on 07.12.16.
 */

//asdasd

public final class PasswordSaverContract {

    private PasswordSaverContract(){
            }

    static final class Users implements BaseColumns{
        final static String TABLE_NAME = "users";

        final static String _ID = BaseColumns._ID;
        final static String COLUMN_PASSWORD = "password";

    }

    public static final class Passwords implements BaseColumns{
        final static String TABLE_NAME = "passwords";

        final static String _ID = BaseColumns._ID;
        public final static String COLUMN_RESOURCE = "resource";
        public final static String COLUMN_LOGIN = "login";
        public final static String COLUMN_PASSWORD = "password";
        final static String COLUMN_USER = "user";
    }
}
