package net.a8pade8.passwordsaver.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
    private var favoriteOnly = false

    @Suppress(names = ["UNCHECKED_CAST"])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recordsListView = findViewById(R.id.listOfRes)
        recordsListView.onItemClickListener = OnItemClickListener { adapterView, view, position, id -> onItemClick(adapterView, view, position, id) }
        showResourceList()
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(findViewById(R.id.mainToolbar))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.main_action_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @Suppress("UNUSED_PARAMETER")
    fun openAddRecordActivity(view: View) {
        startActivity(Intent(this, AddRecordActivity::class.java))
    }

    private fun showResourceList() {
        recordsListView.adapter = getListAdapter()
    }

    private fun getListAdapter(): RecordViewAdapter {
        return if (favoriteOnly) {
            RecordViewAdapter(this, getAllRecordsFromPasswords().filter { it.favorite })
        } else {
            RecordViewAdapter(this, getAllRecordsFromPasswords())
        }
    }

    private fun openResourceView(id: Long) {
        Intent(this, ViewRecordActivity::class.java).let {
            it.putExtra("id", id)
            startActivity(it)
        }
    }

    override fun onResume() {
        super.onResume()
        showResourceList()
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onItemClick(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
        openResourceView((adapterView.getItemAtPosition(position) as Record).id)
    }

    fun showFavorite(item: MenuItem) {
        favoriteOnly = !favoriteOnly
        when (favoriteOnly){
            true -> item.icon.setTint(getColor(R.color.colorContrast))
            else -> item.icon.setTint(getColor(R.color.colorContrastDark))
        }
        showResourceList()
    }
}