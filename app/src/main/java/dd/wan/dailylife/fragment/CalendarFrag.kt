package dd.wan.dailylife.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import dd.wan.dailylife.R
import dd.wan.dailylife.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_calendar2.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar
import kotlin.collections.ArrayList

class CalendarFrag : Fragment() {

    private var mSelectedPageIndex = 1
    var list = ArrayList<CalendarFragment>()
    var listday = arrayListOf<String>("Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun")
    var sdfMonth = SimpleDateFormat("MMMM", Locale.ENGLISH)
    var sdfYear = SimpleDateFormat("yyyy", Locale.ENGLISH)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_calendar2, container, false)

        var calendar: Calendar = Calendar.getInstance(Locale.getDefault())
        var prevMonth = calendar.clone() as Calendar
        var nextMonth = calendar.clone() as Calendar
        prevMonth.add(Calendar.MONTH, -1)
        nextMonth.add(Calendar.MONTH, 1)
        var month = sdfMonth.format(calendar.time)
        view.tv_month.text = month
        var year = sdfYear.format(calendar.time)
        view.tv_year.text = year

        // tạo danh sách fragment và setup viewpager
        list.add(CalendarFragment().newInstance(prevMonth, 5)) // mặc định bắt đầu từ thứ 2
        list.add(CalendarFragment().newInstance(calendar, 5))
        list.add(CalendarFragment().newInstance(nextMonth, 5))

        var adapter = ViewPagerAdapter(getChildFragmentManager(), lifecycle, list)

        view.viewPager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                mSelectedPageIndex = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if (mSelectedPageIndex < 1) {
                        for (item in list)
                            item.onPreviousMonth()
                    } else if (mSelectedPageIndex > 1) {
                        for (item in list)
                            item.onNextMonth()
                    }
                    view.viewPager.setCurrentItem(1, false)
                    var cal = list[1].getCurrentCalendar()
                    view.tv_month.text = sdfMonth.format(cal.time)
                    view.tv_year.text = sdfYear.format(cal.time)
                }
            }
        })
        view.viewPager.adapter = adapter
        view.viewPager.offscreenPageLimit = 2
        view.viewPager.setCurrentItem(1, false)
        var adapterDay = DayAdapter(listday)
//        view.setting.setOnClickListener {
//            var popupMenu = PopupMenu(context, it)
//            popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
//            popupMenu.show()
//            popupMenu.setOnMenuItemClickListener {
//                when (it.itemId) {
//                    R.id.mon -> {
//                        changeStart(5) //-2
//                        changeDay("Mon")
//                    }
//                    R.id.tue -> {
//                        changeStart(4) //-3
//                        changeDay("Tue")
//                    }
//                    R.id.wed -> {
//                        changeStart(3) //-4
//                        changeDay("Wed")
//                    }
//                    R.id.thur -> {
//                        changeStart(2) //-5
//                        changeDay("Thur")
//                    }
//                    R.id.fri -> {
//                        changeStart(1)
//                        changeDay("Fri")
//                    }
//                    R.id.sat -> {
//                        changeStart(0)
//                        changeDay("Sat")
//                    }
//                    R.id.sun -> {
//                        changeStart(-1)
//                        changeDay("Sun")
//                    }
//                }
//                adapterDay.notifyDataSetChanged()
//                false
//            }
//        }

        // hiển thị ngày trong tuần

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(context, 7)
        view.dayOfWeek.layoutManager = layoutManager
        view.dayOfWeek.setHasFixedSize(true)
        view.dayOfWeek.setItemViewCacheSize(7)
        view.dayOfWeek.adapter = adapterDay
        return view
    }

    fun changeStart(start: Int) {
        for (item in list) {
            item.startWeekOn(start)
        }
    }

    fun changeDay(string: String) {
        var listPhu = ArrayList<String>()
        while (!listday.get(0).equals(string)) {
            listPhu.add(listday.get(0))
            listday.removeAt(0)
        }
        listday.addAll(listPhu)
    }

    class DayAdapter(var list: ArrayList<String>) : RecyclerView.Adapter<DayAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.custom_days, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.setData(list.get(position))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var textView: TextView = itemView.findViewById(R.id.tv_dayOfW)
            fun setData(string: String) {
                textView.text = string
            }
        }
    }
}