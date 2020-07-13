package net.a8pade8.passwordsaver.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;


import net.a8pade8.passwordsaver.R;
import net.a8pade8.passwordsaver.data.DbserviceKt;
import net.a8pade8.passwordsaver.security.Security;
import net.a8pade8.passwordsaver.util.TestDataGeneratorKt;

import static net.a8pade8.passwordsaver.uilib.MessagesKt.middleToastLong;

public class LoginActivity extends AppCompatActivity {

    private boolean generateTestData = false; // Генерировать тестовые данные
    private boolean crypto = true; //Определеят шифровать ли данные, задел на будущее
    private Security security;
    private EditText passwordIn;
    private int attemptPassword = 3;
    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;
    private final String BLOCK_TIME = "blockTime";
    private final String ATTEMPT_COUNT = "attempts";

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        security = Security.getInstance(this);
        setContentView(R.layout.activity_login);
        DbserviceKt.loading(this, crypto);
        TestDataGeneratorKt.generateTestData(this, generateTestData); //Генерация тестовых данных
        if (security.getPassword().isEmpty()) {
            openAddUserActivity(null);
        }
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
        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(findViewById(R.id.mainToolbar));
    }

    public void openAddUserActivity(View view) {
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
        if (!security.getPassword().equals(passwordIn.getText().toString())) {
            middleToastLong(this, getString(R.string.attemptsWarning) + --attemptPassword);
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
            middleToastLong(this, getString(R.string.noAttempts));
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
