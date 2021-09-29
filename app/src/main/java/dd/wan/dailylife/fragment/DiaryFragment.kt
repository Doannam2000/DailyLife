package dd.wan.dailylife.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dd.wan.dailylife.R
import dd.wan.dailylife.SQLHelper
import dd.wan.dailylife.adapter.DiaryAdapter
import dd.wan.dailylife.model.note
import kotlinx.android.synthetic.main.fragment_calendar2.view.*


class DiaryFragment : Fragment() {


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_diary, container, false)
        val sqlHelper = SQLHelper(context)
        var list:ArrayList<note> = sqlHelper.getAllDB()
        list.sortBy {  it.date }
        var adapter = DiaryAdapter(list)
        var recycler:RecyclerView = view.findViewById(R.id.listDiary)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.setHasFixedSize(true)
        recycler.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        recycler.adapter = adapter
        return view
    }

}