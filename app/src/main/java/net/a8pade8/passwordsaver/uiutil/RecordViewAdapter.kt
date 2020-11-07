package net.a8pade8.passwordsaver.uiutil

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import net.a8pade8.passwordsaver.R
import net.a8pade8.passwordsaver.data.Record
import net.a8pade8.passwordsaver.databinding.RecordRowBinding

class RecordViewAdapter(private val activity: Activity, private val recordList: List<Record>) : BaseAdapter() {

    override fun getCount(): Int {
        return recordList.size
    }

    override fun getItem(i: Int): Any? {
        return recordList[i]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.record_row, null)
            var binding: RecordRowBinding
            view?.let {
                binding = DataBindingUtil.bind(it)!!
                binding.record = recordList[position]
                it.tag = binding
            }
        } else {
            (view.tag as RecordRowBinding).record = recordList[position]
        }
        return view
    }

    fun getList() = recordList
}
