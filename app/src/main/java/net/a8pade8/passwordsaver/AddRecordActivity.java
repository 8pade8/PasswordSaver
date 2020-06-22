package net.a8pade8.passwordsaver;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import net.a8pade8.passwordsaver.a8pade8Lib1.Messages;
import net.a8pade8.passwordsaver.data.PSDBHelper;
import net.a8pade8.passwordsaver.data.db;

public class AddRecordActivity extends AppCompatActivity {

    private EditText editTextResourse, editTextLogin, editTextPassword, editTextPasswordReplay;
    private ToggleButton buttonSite, buttonFade, buttonEmail;
    private Button buttonReady;

    private int inputTypeLogin;
    private int inputTypeResourse;

    private String resource;
    private String login;
    private String password;
    private String passwordRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordReplay = findViewById(R.id.editTextPasswordReplay);
        editTextResourse = findViewById(R.id.editTextResourse);

        buttonEmail = findViewById(R.id.toggleButtonEmail);
        buttonFade = findViewById(R.id.toggleButtonFade);
        buttonSite = findViewById(R.id.toggleButtonSite);
        buttonReady = findViewById(R.id.buttonReady);

        inputTypeLogin = editTextLogin.getInputType();
        inputTypeResourse = editTextResourse.getInputType();
    }

    public void onSwitchSite(View view) {
        if (buttonSite.isChecked()) {
            editTextResourse.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            editTextResourse.setInputType(inputTypeResourse);
        }
    }

    public void onSwitchEmail(View view) {
        if (buttonEmail.isChecked()) {
            editTextLogin.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            editTextLogin.setInputType(inputTypeLogin);
        }
    }

    public void onSwitchFade(View view) {
        if (!buttonFade.isChecked()) {
            editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editTextPasswordReplay.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            editTextPasswordReplay.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }

    public void onReady(View view) {

        password = editTextPassword.getText().toString();
        passwordRetry = editTextPasswordReplay.getText().toString();
        login = editTextLogin.getText().toString();
        resource = editTextResourse.getText().toString();

        if (isValuesChecked() && isPasswordsEquals()) {

            if (!isResourceExist()) {
                addRecord();
            } else {
                messageDoubleResource();
            }
        }
    }

    private boolean isPasswordsEquals() {
        if (!password.equals(passwordRetry)) {
            Messages.MiddleToastShort(this, "Пароли не совпадают.");
            return false;
        } else {
            return true;
        }
    }

    private boolean isResourceExist() {
        return db.isContainResourceInPasswords(resource);
    }


    private void messageDoubleResource() {

        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Внимание!");
        ad.setMessage("Указанный ресурс уже существует. Все равно добавить?");
        ad.setCancelable(false);
        ad.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addRecord();
                dialogInterface.cancel();
            }
        });
        ad.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });
        AlertDialog alert = ad.create();
        alert.show();
    }

    private boolean isValuesChecked() {
        return isResourceEmpty()
                && isLoginEmpty()
                && isPasswordEmpty();
    }

    private boolean isResourceEmpty() {
        if (editTextResourse.getText().toString().equals("")) {
            Messages.MiddleToastShort(this, "Поле ввода РЕСУРС не заполнено.");
            return false;
        } else {
            return true;
        }
    }

    private boolean isLoginEmpty() {
        if (editTextLogin.getText().toString().equals("")) {
            Messages.MiddleToastShort(this, "Поле ввода ЛОГИН не заполнено.");
            return false;
        } else {
            return true;
        }
    }

    private boolean isPasswordEmpty() {
        if (editTextPassword.getText().toString().equals("")) {
            Messages.MiddleToastShort(this, "Поле ввода ПАРОЛЬ не заполнено.");
            return false;
        }
        return true;
    }

    private void addRecord() {
        db.addRecordToPasswords(resource, login, password);
        Messages.MiddleToastLong(this, "Запись успешно добавлена.");
        this.finish();
    }

}









































