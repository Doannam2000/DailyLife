package dd.wan.dailylife.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dd.wan.dailylife.R
import dd.wan.dailylife.adapter.CalendarAdapter
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment : Fragment() {
    var dates: ArrayList<Date> = ArrayList()

    lateinit var calendar: Calendar
    var start = 5
    lateinit var adapter: CalendarAdapter
    lateinit var calendarRecycler: RecyclerView

    fun newInstance(calendar: Calendar, start: Int) = CalendarFragment().apply {
        arguments = bundleOf(
            "calendar" to calendar,
            "start" to start
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendar = arguments?.getSerializable("calendar") as Calendar
        start = arguments?.getInt("start")!!
        var view: View = inflater.inflate(R.layout.fragment_calendar, container, false)
        calendarRecycler = view.findViewById(R.id.recycler_calendar)
        calendarRecycler.itemAnimator = null
        // hiển thị ngày tháng
        adapter = CalendarAdapter(dates, calendar)
        updateData()
        setUpCalendar()
        return view
    }

    fun setUpCalendar() {
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(context, 7)

        calendarRecycler.layoutManager = layoutManager
        calendarRecycler.setHasFixedSize(true)
        calendarRecycler.setItemViewCacheSize(42)
//        var dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
//        var dividerItemDecoration1 = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
//        calendarRecycler.addItemDecoration(dividerItemDecoration)
//        calendarRecycler.addItemDecoration(dividerItemDecoration1)
        calendarRecycler.adapter = adapter
    }

    fun updateData() {
        dates.clear()
        var monthCalendar: Calendar = calendar.clone() as Calendar
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
        var firstOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) + start
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstOfMonth)
        var da = calendar.clone() as Calendar
        while (dates.size < 42) {
            dates.add(monthCalendar.time)
            if (dates.size == 7 && (da.get(Calendar.MONTH) != monthCalendar.get(Calendar.MONTH))) {
                dates.clear()
                monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
                continue
            }
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
            if ((dates.size % 7 == 0) && (da.get(Calendar.MONTH) != monthCalendar.get(Calendar.MONTH))) {
                break
            }
        }
    }

    fun onNextMonth() {
        if (this::calendar.isInitialized) {
            calendar.add(Calendar.MONTH, 1)
            updateData()
            adapter.resetColor()
            adapter.notifyDataSetChanged()
        }
    }

    fun onPreviousMonth() {
        if (this::calendar.isInitialized) {
            calendar.add(Calendar.MONTH, -1)
            updateData()
            adapter.resetColor()
            adapter.notifyDataSetChanged()
        }
    }

    fun startWeekOn(start: Int) {
        this.start = start
        if (this::calendar.isInitialized) {
            updateData()
            adapter.resetColor()
            adapter.notifyDataSetChanged()
        }
    }

    fun getCurrentCalendar(): Calendar {
        return calendar
    }
}