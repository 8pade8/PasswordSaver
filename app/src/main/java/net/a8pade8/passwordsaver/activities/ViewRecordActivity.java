package net.a8pade8.passwordsaver.activities;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.zagum.switchicon.SwitchIconView;

import net.a8pade8.passwordsaver.R;
import net.a8pade8.passwordsaver.data.DbserviceKt;
import net.a8pade8.passwordsaver.data.IdIsNotExistException;
import net.a8pade8.passwordsaver.data.Record;
import net.a8pade8.passwordsaver.data.ResourceLoginRepeatException;

import static net.a8pade8.passwordsaver.R.string.copiedToClipboard;
import static net.a8pade8.passwordsaver.R.string.siteNotFound;
import static net.a8pade8.passwordsaver.uiutil.MessagesKt.middleToastLong;
public class ViewRecordActivity extends AppCompatActivity {

    TextView resourceTextView;
    TextView passwordTextView;
    TextView loginTextView;
    TextView commentTextView;
    SwitchIconView passwordToggleButton;
    SwitchIconView favoriteToggleButton;
    Record record;
    String fadeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_view);
        resourceTextView = findViewById(R.id.resourceTextView);
        passwordTextView = findViewById(R.id.password);
        loginTextView = findViewById(R.id.login);
        commentTextView = findViewById(R.id.comment);
        passwordToggleButton = findViewById(R.id.passwordToggleButton);
        favoriteToggleButton = findViewById(R.id.favoriteToggleButton);
        try {
            record = DbserviceKt.getRecordFromPasswords(getIntent().getLongExtra("id", 0));
        } catch (IdIsNotExistException e) {
            middleToastLong(this, getString(R.string.recordIsNotExist));
            finish();
            return;
        }
        fadeString = getString(R.string.passwordMask);
        resourceTextView.setText(record.getResourceName());
        loginTextView.setText(record.getLogin());
        commentTextView.setText(record.getComment());
        favoriteToggleButton.setIconEnabled(record.getFavorite());
        fadePassword();
        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(findViewById(R.id.mainToolbar));
    }

    private void showPassword() {
        passwordTextView.setText(record.getPassword());
    }

    private void fadePassword() {
        passwordTextView.setText(fadeString);
    }

    public void switchFadePassword(View view) {
        passwordToggleButton.switchState(true);
        if (passwordToggleButton.isIconEnabled()) {
            showPassword();
        } else {
            fadePassword();
        }
    }

    public void delete(View view) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.warning))
                .setMessage(getString(R.string.approveDeleteRecord))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.delete), (dialogInterface, i) -> deleteRecord())
                .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.cancel())
                .create()
                .show();
    }

    private void deleteRecord() {
        try {
            DbserviceKt.deleteRecordFromPasswords(record.getId());
            middleToastLong(this, getString(R.string.recordDeleted));
            finish();
        } catch (IdIsNotExistException e) {
            middleToastLong(this, getString(R.string.recordDeleteFailed));
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
        middleToastLong(this, getString(copiedToClipboard));
    }

    public void goToResource(View view) {
        String address = resourceTextView.getText().toString();
        if (!address.startsWith("http")) {
            address = "http://" + address;
        }
        if (Patterns.WEB_URL.matcher(address).matches()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(address));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } else {
            middleToastLong(this, getString(siteNotFound));
        }
    }

    public void switchFavorite(View view) throws ResourceLoginRepeatException, IdIsNotExistException {
        favoriteToggleButton.switchState(true);
        record.setFavorite(favoriteToggleButton.isIconEnabled());
        DbserviceKt.updateRecordInPasswords(record);
    }
}
