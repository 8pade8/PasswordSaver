package net.a8pade8.passwordsaver;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import net.a8pade8.passwordsaver.a8pade8Lib1.Messages;
import net.a8pade8.passwordsaver.data.DbserviceKt;
import net.a8pade8.passwordsaver.data.User;

public class LoginActivity extends AppCompatActivity {

    private boolean isCrypto = true; //Определеят шифровать ли данные, задел на будущее
    private EditText passwordIn;
    private int attemptPassword = 3;
    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;
    private final String BLOCK_TIME = "blockTime";
    private final String ATTEMPT_COUNT = "attempts";
    private User user;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = User.getInstance(this);
        if (user.getPassword().isEmpty()) {
            openAddUserActivity(null);
        }
        setContentView(R.layout.activity_login);
        DbserviceKt.loading(this, isCrypto);
        passwordIn = findViewById(R.id.password);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferencesEditor = preferences.edit();
        if (preferences.contains(ATTEMPT_COUNT)) {
            attemptPassword = preferences.getInt(ATTEMPT_COUNT, 0);
        }
        if (attemptPassword == 0) {
            attemptPassword = 3;
        }
        if (isBlocked()) {
            attemptPassword = 0;
        }
    }

    public void openAddUserActivity(View view){
        Intent openAddUserActivity = new Intent(this, AddUserActivity.class);
        startActivity(openAddUserActivity);
    }

    public void openMainActivity(View view) {
        if (isAttemptExist() && isPasswordExist()) {
            Intent openMainActivity = new Intent(this, MainActivity.class);
            startActivity(openMainActivity);
        }
    }

    private boolean isPasswordExist() {
        if (!user.getPassword().equals(passwordIn.getText().toString())) {
            Messages.MiddleToastShort(this, "Введен неверный пароль, осталось попыток: " + --attemptPassword);

            if (attemptPassword == 0) {
                if (!isBlocked()) {
                    block();
                }
            }
            return false;
        }
        return true;
    }

    private boolean isAttemptExist() {
        if (attemptPassword == 0) {
            Messages.MiddleToastLong(this, "Вы ввели неверный пароль трижды, попробуйте позднее.");
            passwordIn.setText("");
            return false;
        }
        return true;
    }

    private boolean isBlocked() {
        if (preferences.contains(BLOCK_TIME)) {
            long blockTime = preferences.getLong("blockTime", 0);
            long timeNow = System.currentTimeMillis();
            return (timeNow - blockTime) / 60000 <= 5;
        }
        return false;
    }

    private void block() {
        preferencesEditor.putLong(BLOCK_TIME, System.currentTimeMillis());
        preferencesEditor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        preferencesEditor.putInt(ATTEMPT_COUNT, attemptPassword);
        preferencesEditor.apply();
    }
}
