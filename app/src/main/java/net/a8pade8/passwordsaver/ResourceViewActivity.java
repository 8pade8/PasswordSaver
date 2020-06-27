package net.a8pade8.passwordsaver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import net.a8pade8.passwordsaver.a8pade8Lib1.Messages;
import net.a8pade8.passwordsaver.data.Record;
import net.a8pade8.passwordsaver.data.db;

public class ResourceViewActivity extends AppCompatActivity {

    TextView resourceTextView;
    TextView passwordTextView;
    TextView loginTextView;
    Record record;
    String fadeString = "***************";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_view);
        resourceTextView = findViewById(R.id.resource);
        passwordTextView = findViewById(R.id.password);
        loginTextView = findViewById(R.id.login);

        try {
            record = db.getRecordFromPasswords(getIntent().getIntExtra("id", 0));
        } catch (db.IdIsNotExistException e) {
            Messages.MiddleToastShort(this, "Ошибка, запись не найдена");
            finish();
            return;
        }

        resourceTextView.setText(record.getResourceName());
        loginTextView.setText(record.getLogin());
        fadePassword();
    }

    private void showPassword() {
        passwordTextView.setText(record.getPassword());
    }

    private void fadePassword() {
        passwordTextView.setText(fadeString);
    }

    public void switchFadePassword(View view) {
        if (passwordTextView.getText().equals(fadeString)) {
            showPassword();
        } else {
            fadePassword();
        }
    }
}
