package net.a8pade8.passwordsaver.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import net.a8pade8.passwordsaver.R;
import net.a8pade8.passwordsaver.uilib.Messages;
import net.a8pade8.passwordsaver.security.Security;

public class AddUserActivity extends AppCompatActivity {

    private EditText passwordIn, passwordInReplay;
    private String password, passwordReplay;
    private Security security;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        passwordIn = findViewById(R.id.password);
        passwordInReplay = findViewById(R.id.replayPassword);
        security = Security.getInstance(this);
    }

    public void addNewUser(View view) {
        password = passwordIn.getText().toString();
        passwordReplay = passwordInReplay.getText().toString();
        if (passwordChecked()) {
            security.setPassword(password);
            Messages.MiddleToastShort(this, getString(R.string.addingUserSuccessfully));
            this.finish();
        }
        passwordIn.setText("");
        passwordInReplay.setText("");
    }


    private boolean isPasswordsEquals(String pass1, String pass2) {
        if (!pass1.equals(pass2)) {
            Messages.MiddleToastLong(this, getString(R.string.passwordsNotEquals));
            return false;
        }
        return true;
    }

    private boolean isEasyPassword() {
        String[] easyPaswords = {"123qwe", "qwe123", "q12345", "12345q", "qwert1", "1qwert", "qwert0",
                "0qwert", "12qwer", "qwer12"};
        for (String easy : easyPaswords) {
            if (easy.equals(password)) {
                Messages.MiddleToastShort(this, getString(R.string.easyPassword));
                return true;
            }
        }
        return false;
    }

    private boolean passwordChecked() {
        return isPasswordSixSymbols()
                && isPasswordCombined()
                && isPasswordsEquals(password, passwordReplay)
                && !isEasyPassword();
    }

    private boolean isPasswordSixSymbols() {
        if (password.length() >= 6) {
            return true;
        } else {
            Messages.MiddleToastShort(this, getString(R.string.demandForPasswordLength));
            return false;
        }
    }

    private boolean isPasswordCombined() {
        int letter = 0, digit = 0;
        for (int i = 0; i < password.length() - 1; i++) {
            if (Character.isLetter(password.charAt(i))) letter++;
            if (Character.isDigit(password.charAt(i))) digit++;
        }
        if (letter > 0 && digit > 0) return true;
        else {
            Messages.MiddleToastShort(this, getString(R.string.demandForPasswordCharacters));
            return false;
        }
    }
}