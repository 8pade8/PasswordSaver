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
        String[] easyPaswords = {"00000", "11111", "22222", "33333", "44444", "55555", "666666",
                "77777", "888888", "99999"};
        for (String easy : easyPaswords) {
            if (easy.equals(password)) {
                Messages.MiddleToastShort(this, getString(R.string.easyPassword));
                return true;
            }
        }
        return false;
    }

    private boolean passwordChecked() {
        return isPasswordFiveSymbols()
                && isPasswordNumber()
                && isPasswordsEquals(password, passwordReplay)
                && !isEasyPassword();
    }

    private boolean isPasswordFiveSymbols() {
        if (password.length() == 5) {
            return true;
        } else {
            Messages.MiddleToastShort(this, getString(R.string.demandForPasswordLength));
            return false;
        }
    }

    private boolean isPasswordNumber() {
        try {
            Integer.parseInt(password);
            return true;
        } catch (NumberFormatException e) {
            Messages.MiddleToastShort(this, getString(R.string.demandForPasswordCharacters));
            return false;
        }
    }
}