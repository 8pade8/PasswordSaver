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
import net.a8pade8.passwordsaver.uiutil.middleToastLong
import net.a8pade8.passwordsaver.util.exportPasswordsToFile
import net.a8pade8.passwordsaver.util.exportPasswordsToTxtFile
import net.a8pade8.passwordsaver.util.importPasswordsFromFile
import net.a8pade8.passwordsaver.util.openActivity
import net.a8pade8.passwordsaver.util.verifyStoragePermissions
import java.util.Locale


class MainActivity : AppCompatActivity() {
    val CREATE_JSON_FILE = 1
    val CREATE_TXT_FILE = 2
    val PICK_JSON_FILE = 3
    private lateinit var recordsListView: ListView
    private var favoriteOnly = false
    private var searchString: String = ""

    @Suppress(names = ["UNCHECKED_CAST"])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recordsListView = findViewById(R.id.listOfRes)
        recordsListView.onItemClickListener =
            OnItemClickListener { adapterView, view, position, id ->
                onItemClick(
                    adapterView,
                    view,
                    position,
                    id
                )
            }
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
            true -> item.icon?.setTint(getColor(R.color.colorContrast))
            else -> item.icon?.setTint(getColor(R.color.colorContrastDark))
        }
        showResourceList()
    }

    @Suppress("UNUSED_PARAMETER")
    fun exportToFile(item: MenuItem) {
        if (recordsListView.adapter.count == 0) {
            middleToastLong(this, getString(R.string.thereAreNoRecordsToExport))
            return
        }
        if (verifyStoragePermissions(this)) {
            createFile("application/json", "passwords.json", CREATE_JSON_FILE)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun importFromFile(item: MenuItem) {
        if (verifyStoragePermissions(this)) {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/json"
            }
            startActivityForResult(intent, PICK_JSON_FILE)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun exportToTxtFile(item: MenuItem) {
        if (recordsListView.adapter.count == 0) {
            middleToastLong(this, getString(R.string.thereAreNoRecordsToExport))
            return
        }
        if (verifyStoragePermissions(this)) {
            createFile("application/txt", "passwords.txt", CREATE_TXT_FILE)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (resultCode == RESULT_OK) {
            resultData?.data?.let {
                var records = (recordsListView.adapter as RecordViewAdapter).getList()
                val contentResolver = getContentResolver()
                if (requestCode == CREATE_TXT_FILE) {
                    contentResolver.openOutputStream(it)?.let {
                        exportPasswordsToTxtFile(it, records, this)
                    }
                } else if (requestCode == CREATE_JSON_FILE) {
                    contentResolver.openOutputStream(it)?.let {
                        exportPasswordsToFile(it, records, this)
                    }
                } else if (requestCode == PICK_JSON_FILE) {
                    contentResolver.openInputStream(it)?.let {
                        importPasswordsFromFile(it, this@MainActivity)
                        showAllResourceList()
                    }
                }else{
                }
            }
        }
    }

    private fun MainActivity.createFile(type: String, defaultFileName: String, requestCode: Int) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            this.type = type
            putExtra(Intent.EXTRA_TITLE, defaultFileName)
        }
        startActivityForResult(intent, requestCode)
    }
}