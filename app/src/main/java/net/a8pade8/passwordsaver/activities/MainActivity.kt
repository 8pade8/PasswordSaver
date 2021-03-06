package net.a8pade8.passwordsaver.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import net.a8pade8.passwordsaver.R
import net.a8pade8.passwordsaver.data.Record
import net.a8pade8.passwordsaver.data.getAllRecordsFromPasswords
import net.a8pade8.passwordsaver.uiutil.RecordViewAdapter
import net.a8pade8.passwordsaver.util.exportPasswordsToFile
import net.a8pade8.passwordsaver.util.openActivity
import net.a8pade8.passwordsaver.util.verifyStoragePermissions
import net.rdrei.android.dirchooser.DirectoryChooserActivity
import net.rdrei.android.dirchooser.DirectoryChooserConfig
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var recordsListView: ListView
    private var favoriteOnly = false
    private var searchString: String = ""
    private val REQUEST_DIRECTORY = 0

    @Suppress(names = ["UNCHECKED_CAST"])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recordsListView = findViewById(R.id.listOfRes)
        recordsListView.onItemClickListener = OnItemClickListener { adapterView, view, position, id -> onItemClick(adapterView, view, position, id) }
        showAllResourceList()
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(findViewById(R.id.mainToolbar))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.main_action_menu, menu)
        val searchBar = menu?.findItem(R.id.searchBar)?.actionView as SearchView
        searchBar.queryHint = resources.getString(R.string.search)
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchString = newText
                    showResourceList()
                } else {
                    searchString = ""
                    showResourceList()
                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    @Suppress("UNUSED_PARAMETER")
    fun openAddRecordActivity(view: View) {
        openActivity(AddRecordActivity::class.java)
    }

    private fun showAllResourceList() {
        recordsListView.adapter = RecordViewAdapter(this, getAllRecordsFromPasswords())
    }

    private fun showResourceList() {
        recordsListView.adapter = getListAdapter()
    }

    private fun getListAdapter(): RecordViewAdapter {
        var list = getAllRecordsFromPasswords()
        if (searchString.isNotBlank()) {
            list = list.filter { it.resourceName.toLowerCase(Locale.ROOT).contains(searchString) }
        }
        if (favoriteOnly) {
            list = list.filter { it.favorite }
        }
        return RecordViewAdapter(this, list)
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
        when (favoriteOnly) {
            true -> item.icon.setTint(getColor(R.color.colorContrast))
            else -> item.icon.setTint(getColor(R.color.colorContrastDark))
        }
        showResourceList()
    }

    @Suppress("UNUSED_PARAMETER")
    fun exportToFile(item: MenuItem) {
        if (recordsListView.adapter.count > 0 && verifyStoragePermissions(this)) {
            val chooserIntent = Intent(this, DirectoryChooserActivity::class.java)

            val config = DirectoryChooserConfig.builder()
                    .newDirectoryName("DirChooserSample")
                    .allowReadOnlyDirectory(true)
                    .allowNewDirectoryNameModification(true)
                    .build()
            chooserIntent.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config)
            startActivityForResult(chooserIntent, REQUEST_DIRECTORY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_DIRECTORY) {
            if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                exportPasswordsToFile(
                        data?.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR).toString(),
                        (recordsListView.adapter as RecordViewAdapter).getList(),this)
            }
        }
    }

}