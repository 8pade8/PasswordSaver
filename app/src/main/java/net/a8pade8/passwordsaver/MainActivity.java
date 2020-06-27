package net.a8pade8.passwordsaver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.a8pade8.passwordsaver.data.Record;
import net.a8pade8.passwordsaver.data.db;

public class MainActivity extends AppCompatActivity {

    private ListView recordsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordsListView = findViewById(R.id.listOfRes);
        recordsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                int resourceId = ((Record) adapterView.getAdapter().getItem(position)).getId();
                openResourceView(resourceId);
            }
        });

        listResourcesShow();
    }

    public void openAddRecordActivity(View view) {
        Intent openAddRecordActivity = new Intent(this, AddRecordActivity.class);
        startActivity(openAddRecordActivity);
    }

    private void listResourcesShow() {
        ArrayAdapter<Record> listAdapter =
                new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, db.getAllRecordsFromPasswords());
        recordsListView.setAdapter(listAdapter);
    }

    private void openResourceView(int id) {
        Intent openResourceViewActivity = new Intent(this, ResourceViewActivity.class);
        openResourceViewActivity.putExtra("id", id);
        startActivity(openResourceViewActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listResourcesShow();
    }

}
