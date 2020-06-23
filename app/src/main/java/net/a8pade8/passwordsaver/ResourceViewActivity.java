package net.a8pade8.passwordsaver;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.a8pade8.passwordsaver.a8pade8Lib1.Messages;
import net.a8pade8.passwordsaver.data.PasswordSaverContract;
import net.a8pade8.passwordsaver.data.db;

public class ResourceViewActivity extends AppCompatActivity {

    TextView resource;
    TextView password;
    TextView login;
    String resourceName;
    Cursor data;
    String fadeString = "***************";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_view);
        resource = findViewById(R.id.resource);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        setResourceName();
        resource.setText(resourceName);
        data = db.getResourceOfPasswords(resourceName);
        data.moveToFirst();
        login.setText(data.getString(data.getColumnIndex(PasswordSaverContract.Passwords.COLUMN_LOGIN)));
        fadePassword();
    }

    private void setResourceName() {
        Intent intent = getIntent();
        resourceName = intent.getStringExtra("resource");
    }

    private void showPassword() {
        password.setText(data.getString(data.getColumnIndex(PasswordSaverContract.Passwords.COLUMN_PASSWORD)));
    }

    private void fadePassword() {
        password.setText(fadeString);
    }

    public void switchFadePassword(View view) {
        if (password.getText().equals(fadeString)) {
            showPassword();
        } else {
            fadePassword();
        }
    }
}

// Another test commit