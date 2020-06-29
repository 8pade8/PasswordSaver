package net.a8pade8.passwordsaver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import net.a8pade8.passwordsaver.a8pade8Lib1.Messages;
import net.a8pade8.passwordsaver.data.DbserviceKt;
import net.a8pade8.passwordsaver.data.Record;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import kotlin.Suppress;

public class MainActivity extends AppCompatActivity {

    private ListView recordsListView;


    @Override
    @Suppress(names = "UNCHECKED_CAST")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recordsListView = findViewById(R.id.listOfRes);
        recordsListView.setOnItemClickListener(this::onItemClick);

        listResourcesShow();
    }

    public void openAddRecordActivity(View view) {
        Intent openAddRecordActivity = new Intent(this, AddRecordActivity.class);
        startActivity(openAddRecordActivity);
    }

    private void listResourcesShow() {
        SimpleAdapter adapter = new SimpleAdapter(this,
                DbserviceKt.getAllRecordsFromPasswords().stream().map(Record::toMap).collect(Collectors.toList()),
                android.R.layout.simple_list_item_2,
                new String[]{"resourceName", "login"},
                new int[]{android.R.id.text1, android.R.id.text2});
        recordsListView.setAdapter(adapter);
    }

    private void openResourceView(long id) {
        Intent openResourceViewActivity = new Intent(this, ResourceViewActivity.class);
        openResourceViewActivity.putExtra("id", id);
        startActivity(openResourceViewActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listResourcesShow();
    }

    @SuppressWarnings("unchecked")
    private void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        HashMap<String, String> item = (HashMap<String, String>) adapterView.getAdapter().getItem(position);
        long resourceId = Long.parseLong(Objects.requireNonNull(item.get("id")));
        openResourceView(resourceId);
    }
}
