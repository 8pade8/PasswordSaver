package net.a8pade8.passwordsaver.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView

import androidx.appcompat.app.AppCompatActivity

import net.a8pade8.passwordsaver.R
import net.a8pade8.passwordsaver.data.Record
import net.a8pade8.passwordsaver.data.getAllRecordsFromPasswords
import net.a8pade8.passwordsaver.uiutil.RecordViewAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var recordsListView: ListView

    @Suppress(names = ["UNCHECKED_CAST"])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recordsListView = findViewById(R.id.listOfRes)
        recordsListView.onItemClickListener = OnItemClickListener { adapterView, view, position, id -> onItemClick(adapterView, view, position, id) }
        listResourcesShow()
    }

    fun openAddRecordActivity(view: View?) {
        startActivity(Intent(this, AddRecordActivity::class.java))
    }


    private fun listResourcesShow() {
        recordsListView.adapter = RecordViewAdapter(this, getAllRecordsFromPasswords())
    }

    private fun openResourceView(id: Long) {
        Intent(this, ResourceViewActivity::class.java).let{
            it.putExtra("id", id)
            startActivity(it)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        listResourcesShow()
    }

    private fun onItemClick(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
        openResourceView((adapterView.getItemAtPosition(position) as Record).id)
    }
}