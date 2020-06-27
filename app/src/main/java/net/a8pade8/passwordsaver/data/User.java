package net.a8pade8.passwordsaver.data;

import android.content.Context;
import android.content.SharedPreferences;

public class User {
    private final static String PASSWORD_NAME = "USER_PASSWORD";
    private static SharedPreferences sharedPreferences;
    private static User INSTANCE = null;

    private User(Context context) {
        sharedPreferences = context.getSharedPreferences("net.a8pade8.passwordsaver", Context.MODE_PRIVATE);
    }

    public static User getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (User.class) {
                if (INSTANCE == null) {
                    INSTANCE = new User(context);
                }
            }
        }
        return INSTANCE;
    }

    public String getPassword() {
        return sharedPreferences.getString(PASSWORD_NAME, "");
    }

    public void setPassword(String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PASSWORD_NAME, password);
        editor.apply();
        String result = getPassword();
    }
}
