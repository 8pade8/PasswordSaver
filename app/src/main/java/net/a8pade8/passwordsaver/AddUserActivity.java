package net.a8pade8.passwordsaver;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.a8pade8.passwordsaver.a8pade8Lib1.Messages;
import net.a8pade8.passwordsaver.data.PSDBHelper;
import net.a8pade8.passwordsaver.data.PasswordSaverContract;
import net.a8pade8.passwordsaver.data.db;

import static net.a8pade8.passwordsaver.data.db.DB;

public class AddUserActivity extends AppCompatActivity {

    private EditText passwordIn, passwordInReplay;
    private String password, passwordReplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        passwordIn = findViewById(R.id.password);
        passwordInReplay = findViewById(R.id.replayPassword);

    }

    public void addNewUser(View view) {
        password = passwordIn.getText().toString();
        passwordReplay = passwordInReplay.getText().toString();
        if (passwordChecked()) {
            db.insertPasswordToUsers(password);
            Messages.MiddleToastShort(this, "Пользователь успешно добавлен");
            this.finish();
        }
        passwordIn.setText("");
        passwordInReplay.setText("");
    }


    private boolean isPasswordsEquals(String pass1, String pass2) {
        if (!pass1.equals(pass2)) {
            Messages.MiddleToastLong(this, "Пароли не совпадают");
            return false;
        }
        return true;
    }

    private boolean isEasyPassword() {
        String[] easyPaswords = {"00000", "11111", "22222", "33333", "44444", "55555", "666666",
                "77777", "888888", "99999"};
        for (String easy : easyPaswords) {
            if (easy.equals(password)) {
                Messages.MiddleToastShort(this, "Слишком легкий пароль, введите другой");
                return true;
            }
        }
        return false;
    }

    private boolean passwordChecked() {
        return isPasswordFiveSymbols()
                && isPasswordNumber()
                && isPasswordsEquals(password, passwordReplay)
                && !isEasyPassword()
                && !isUserExist();
    }

    private boolean isPasswordFiveSymbols() {
        if (password.length() == 5) {
            return true;
        } else {
            Messages.MiddleToastShort(this, "Пароль должен состоять из ПЯТИ цифр");
            return false;
        }
    }

    private boolean isPasswordNumber() {
        try {
            Integer.parseInt(password);
            return true;
        } catch (NumberFormatException e) {
            Messages.MiddleToastShort(this, "Пароль должен состоять ТОЛЬКО из цифр");
            return false;
        }
    }

    private boolean isUserExist() {
        if (db.isContainPasswordInUsers(password)) {
            Messages.MiddleToastShort(this, "Такой пользователь уже существует");
            return true;
        } else {
            return false;
        }
    }


}