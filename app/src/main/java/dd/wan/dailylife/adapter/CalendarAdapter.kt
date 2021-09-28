package dd.wan.dailylife.adapter

import android.annotation.SuppressLint
import android.content.Context
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
import dd.wan.dailylife.R


class CalendarAdapter(var list: ArrayList<Date>, var currentDate: Calendar) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    var daySelected = Calendar.getInstance().time
    var itemSelected = -1
    var posision = -1
    var col: Int = Color.TRANSPARENT
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
            if (itemSelected == adapterPosition) {
                layout.setBackgroundColor(col)
            } else {
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
                    posision = adapterPosition
                }
                gestureDetector.onTouchEvent(event)
            }
        }
    }

    inner class GestureListener : SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            itemSelected = posision
            daySelected = list.get(itemSelected)
            col = Color.CYAN
            notifyDataSetChanged()
            return true
        }

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            itemSelected = posision
            daySelected = list.get(itemSelected)
            val rnd = Random()
            val color: Int =
                Color.argb(
                    255,
                    rnd.nextInt(127) + 127,
                    rnd.nextInt(127) + 127,
                    rnd.nextInt(127) + 127
                )
            col = color
            notifyDataSetChanged()
            return true
        }
    }
}

// Double click 2
//            var i = 0
//            var mHandler: Handler = Handler()
//            layout.setOnTouchListener(object : View.OnTouchListener {
//                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                    itemSelected = adapterPosition
//                    daySelected = sdfDate.format(list.get(adapterPosition))
//                    i++
//                    if (i == 1) {
//                        mHandler.postDelayed(Runnable {
//                            if (i != 0) {
//                                col = Color.CYAN
//                                notifyDataSetChanged()
//                            }
//                            i = 0
//                        }, 250)
//                    }
//                    if (i == 2) {
//                        i = 0
//                        val color: Int =
//                            Color.argb(
//                                255,
//                                rnd.nextInt(127) + 127,
//                                rnd.nextInt(127) + 127,
//                                rnd.nextInt(127) + 127
//                            )
//                        col = color
//                        notifyDataSetChanged()
//                    }
//                    return false
//                }
//            })
