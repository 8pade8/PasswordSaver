package net.a8pade8.passwordsaver.security;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

import static net.a8pade8.passwordsaver.uiutil.MessagesKt.middleToastLong;

public class Security {
    private final static String USER_PASSWORD = "USER_PASSWORD";
    private final static String CRYPTO_KEY = "CRYPTO_KEY";
    private static SharedPreferences sharedPreferences;
    private static Security INSTANCE = null;

    private Security(Context context) throws GeneralSecurityException, IOException {

        MasterKey masterKey = new MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();
        sharedPreferences = EncryptedSharedPreferences.create(
                context,
                "secret_shared_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public static Security getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (Security.class) {
                if (INSTANCE == null) {
                    try {
                        INSTANCE = new Security(context);
                    } catch (GeneralSecurityException | IOException e) {
                        middleToastLong(context, "Ошибка досутпа к файлу настроек приложения!");
                        e.printStackTrace();
                    }
                }
            }
        }
        return INSTANCE;
    }

    public String getPassword() {

        return sharedPreferences.getString(USER_PASSWORD, "");
    }

    public String getCryptoKey() {
        String key = sharedPreferences.getString(CRYPTO_KEY, "");
        if (key == null || key.isEmpty()) {
            SecureRandom random = new SecureRandom();
            int leftLimit = 48;
            int rightLimit = 122;
            int targetStringLength = 16;
            StringBuilder buffer = new StringBuilder(targetStringLength);
            for (int i = 0; i < targetStringLength; i++) {
                int randomLimitedInt = leftLimit + (int)
                        (random.nextFloat() * (rightLimit - leftLimit + 1));
                buffer.append((char) randomLimitedInt);
            }
            String generatedString = buffer.toString();
            setCryptoKey(generatedString);
            return generatedString;
        } else {
            return key;
        }
    }

    private void setCryptoKey(String generatedString) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CRYPTO_KEY, generatedString);
        editor.apply();
    }

    public void setPassword(String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_PASSWORD, password);
        editor.apply();
    }
}
