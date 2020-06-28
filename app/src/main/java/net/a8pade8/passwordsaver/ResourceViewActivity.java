package net.a8pade8.passwordsaver;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

    public void delete(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Внимание!")
                .setMessage("Удалить запись?")
                .setCancelable(false)
                .setPositiveButton("Удалить", (dialogInterface, i) -> deleteRecord())
                .setNegativeButton("Отмена", (dialogInterface, i) -> dialogInterface.cancel())
                .create()
                .show();
    }

    private void deleteRecord() {
        try {
            db.deleteRecordFromPasswords(record.getId());
            Messages.MiddleToastLong(this, "Запись удалена");
            finish();
        } catch (db.IdIsNotExistException e) {
            Messages.MiddleToastLong(this, "Не удалось удалить запись");
            e.printStackTrace();
        }
    }
}
