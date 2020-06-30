package net.a8pade8.passwordsaver;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.a8pade8.passwordsaver.a8pade8Lib1.Messages;
import net.a8pade8.passwordsaver.data.DbserviceKt;
import net.a8pade8.passwordsaver.data.IdIsNotExistException;
import net.a8pade8.passwordsaver.data.Record;

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
            record = DbserviceKt.getRecordFromPasswords(getIntent().getLongExtra("id", 0));
        } catch (IdIsNotExistException e) {
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
            DbserviceKt.deleteRecordFromPasswords(record.getId());
            Messages.MiddleToastLong(this, "Запись удалена");
            finish();
        } catch (IdIsNotExistException e) {
            Messages.MiddleToastLong(this, "Не удалось удалить запись");
            e.printStackTrace();
        }
    }

    public void edit(View view) {
        startActivity(new Intent(this, EditRecordActivity.class).putExtra("id", record.getId()));
        finish();
    }

    public void copyInBuffer(View view) {
        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("password", record.getPassword());
        clipboard.setPrimaryClip(clip);
    }
}
