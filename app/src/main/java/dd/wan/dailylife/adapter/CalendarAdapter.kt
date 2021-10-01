package dd.wan.dailylife.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import android.view.GestureDetector
import dd.wan.dailylife.AddDiaryActivity
import dd.wan.dailylife.R
import java.text.SimpleDateFormat


class CalendarAdapter(
    var list: ArrayList<Date>,
    var currentDate: Calendar,
    var listDate: ArrayList<Date>
) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    var daySelected = Calendar.getInstance().time
    val sdf = SimpleDateFormat("E MMM dd yyyy", Locale.getDefault())
    var itemSelected = -1
    var col: Int = Color.CYAN
    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_item, parent, false)
        context = parent.context
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData()
    }

    fun resetColor() {
        itemSelected = -1
        for (i in 0 until list.size) {
            if (list.get(i) == daySelected) {
                itemSelected = i
                break
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("ClickableViewAccessibility")
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.tv_day)
        var layout: ConstraintLayout = itemView.findViewById(R.id.constraint)
        fun setData() {
            var monthDate = list.get(adapterPosition)
            var calendar: Calendar = Calendar.getInstance()
            calendar.time = monthDate
            var day = calendar.get(Calendar.DAY_OF_MONTH)
            var month = calendar.get(Calendar.MONTH) + 1
            var year = calendar.get(Calendar.YEAR)
            var currentMonth = currentDate.get(Calendar.MONTH) + 1
            var currentYear = currentDate.get(Calendar.YEAR)
            if (month == currentMonth && year == currentYear) {
                setColor()
            } else {
                textView.alpha = 0.2F
            }
            textView.text = day.toString()
            if (adapterPosition == itemSelected) {
                layout.setBackgroundColor(col)
            } else {
                if (check(list.get(adapterPosition)))
                    layout.setBackgroundColor(Color.parseColor("#F6CFD6"))
                else
                    layout.setBackgroundColor(Color.parseColor("#f8f8f8"))
            }
        }

        fun setColor() {
            textView.alpha = 1F
        }

        init {
            var gestureDetector = GestureDetector(context, GestureListener())
            layout.setOnTouchListener { v, event ->
                if (adapterPosition != -1) {
                    itemSelected = adapterPosition
                }
                gestureDetector.onTouchEvent(event)
            }
        }
    }

    inner class GestureListener : SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            itemSelected = itemSelected
            daySelected = list.get(itemSelected)
            col = Color.CYAN
            notifyDataSetChanged()
            return true
        }

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            var intent = Intent(context,AddDiaryActivity::class.java)
            intent.putExtra("date",list.get(itemSelected))
            context.startActivity(intent)
            return true
        }
    }

    fun check(date: Date): Boolean {
        for (ite in listDate) {
            if (sdf.format(ite).equals(sdf.format(date))) {
                return true
            }
        }
        return false
    }
}
