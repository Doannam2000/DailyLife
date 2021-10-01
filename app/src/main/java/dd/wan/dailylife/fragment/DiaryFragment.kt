package dd.wan.dailylife.fragment

import android.content.Context
import android.graphics.Color
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dd.wan.dailylife.R
import dd.wan.dailylife.SQLHelper
import dd.wan.dailylife.adapter.DiaryAdapter
import dd.wan.dailylife.model.Note
import kotlinx.android.synthetic.main.fragment_diary.*
import kotlinx.android.synthetic.main.fragment_diary.view.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import dd.wan.dailylife.model.SharedViewModel


class DiaryFragment : Fragment() {
    var check = true

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_diary, container, false)
        // lấy dữ liệu đổ vào recyclerView
        val sqlHelper = SQLHelper(context)
        var list: ArrayList<Note> = sqlHelper.getAllDB()

        val sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.saveData(list)
        var listP = sqlHelper.getAllDB()
        var btnSearch: ImageView = view.findViewById(R.id.btnSearch)
        var recycler: RecyclerView = view.findViewById(R.id.listDiary)
        var txtSort: TextView = view.findViewById(R.id.txtSort)
        var editSearch: EditText = view.findViewById(R.id.editSearch)
        list.sortByDescending { it.date }
        var adapter = DiaryAdapter(list)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.setHasFixedSize(true)
        recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
//        adapter.setCallBack { posision, it ->
//            var popMenu = PopupMenu(context, it)
//            popMenu.menuInflater.inflate(R.menu.menu_diary, popMenu.menu)
//            popMenu.show()
//            popMenu.setOnMenuItemClickListener {
//                when (it.itemId) {
//                    R.id.itemDelete -> {
//                        if (SQLHelper(context).deleteDB(list.get(posision).date) > -1) {
//                            Toast.makeText(context, "Xóa thành công ", Toast.LENGTH_SHORT).show()
//                            list.removeAt(posision)
//                            sharedViewModel.saveData(list)
//                            adapter.notifyDataSetChanged()
//                        } else
//                            Toast.makeText(context, "Xóa không thành công ", Toast.LENGTH_SHORT)
//                                .show()
//                    }
//                }
//                false
//            }
//        }
        recycler.adapter = adapter

        // xử lý button Sắp xếp
        txtSort.setOnClickListener {
            if (check) {
                list.sortBy { it.date }
                check = false
                adapter.notifyDataSetChanged()
            } else {
                list.sortByDescending { it.date }
                check = true
                adapter.notifyDataSetChanged()
            }
        }
        val imm: InputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        editSearch.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                imm.showSoftInput(editSearch, InputMethodManager.HIDE_IMPLICIT_ONLY)
            } else {
                imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0)
            }
        }
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var text = editSearch.text.toString()
                list.clear()
                for (item in listP) {
                    if (item.string.uppercase().contains(text.uppercase()) || item.title.uppercase()
                            .contains(text.uppercase())
                    ) {
                        list.add(item)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        btnSearch.setOnClickListener {
            if (check) {
                editSearch.visibility = View.VISIBLE
                txtSort.visibility = View.GONE
                editSearch.requestFocus()
                btnSearch.setBackgroundColor(Color.parseColor("#F4DBDB"))
                check = false
            } else {
                editSearch.visibility = View.GONE
                txtSort.visibility = View.VISIBLE
                btnSearch.setBackgroundColor(Color.TRANSPARENT)
                check = true
            }
        }
        return view
    }

}