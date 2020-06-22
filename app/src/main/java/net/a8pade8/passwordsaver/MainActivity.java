package net.a8pade8.passwordsaver;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import net.a8pade8.passwordsaver.data.PasswordSaverContract;
import net.a8pade8.passwordsaver.data.db;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listResources;
    private ArrayAdapter<String> listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listResources = findViewById(R.id.listOfRes);

        listResources.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view;
                openResourceView(textView.getText().toString());
            }
        });

        listResourcesShow();
    }

    public void openAddRecordActivity(View view) {
        Intent openAddRecordActivity = new Intent(this, AddRecordActivity.class);
        startActivity(openAddRecordActivity);


    }

    private void listResourcesShow() {
        listAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item,
                cursorToArray(db.getAllResourcesOfPasswords()));
        listResources.setAdapter(listAdapter);
    }


    private ArrayList<String> cursorToArray(Cursor cursor) {
        ArrayList<String> as = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                as.add(cursor.getString(cursor.getColumnIndex(PasswordSaverContract.Passwords.COLUMN_RESOURCE)));
            } while (cursor.moveToNext());

        }
        cursor.close();
        return as;
    }

    private void openResourceView(String resource) {
        Intent openResourceViewActivity = new Intent(this, ResourceViewActivity.class);
        openResourceViewActivity.putExtra("resource", resource);
        startActivity(openResourceViewActivity);
    }


    @Override
    protected void onResume() {
        super.onResume();
        listResourcesShow();
    }

}
