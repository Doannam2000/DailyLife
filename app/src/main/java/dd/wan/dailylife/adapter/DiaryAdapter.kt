package dd.wan.dailylife.adapter

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import dd.wan.dailylife.AddDiaryActivity
import dd.wan.dailylife.R
import dd.wan.dailylife.SQLHelper
import dd.wan.dailylife.model.Note
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.P)
class DiaryAdapter(var list: ArrayList<Note>) : RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {
    val sdfDate = SimpleDateFormat("dd")
    val sdfDay = SimpleDateFormat("dd/MM/yyyy")
    val sdfMonth = SimpleDateFormat("MM")
    lateinit var context: Context

    lateinit var itemClick: (position: Int,view:View) -> Unit

    fun setCallBack(click: (position: Int,view:View) -> Unit) {
        itemClick = click
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_diary, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiaryAdapter.ViewHolder, position: Int) {
        holder.setData()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtDay: TextView = itemView.findViewById(R.id.txtDay)
        var txtDate: TextView = itemView.findViewById(R.id.txtDate)
        var txtContent: TextView = itemView.findViewById(R.id.txtContent)
        var txtMonth: TextView = itemView.findViewById(R.id.txtMonth)
        var txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        var layout: ConstraintLayout = itemView.findViewById(R.id.itemDiary)
//        var btnMore: ImageView = itemView.findViewById(R.id.btnMore)
        fun setData() {
            var item = list.get(adapterPosition)
            var date = item.date
            txtDay.text = sdfDate.format(date)
            txtDate.text = "Ngày :" + sdfDay.format(date)
            txtMonth.text = "Th" + sdfMonth.format(date)
            txtContent.text = "✎ " + item.string
            txtTitle.text = item.title
        }

        init {
            layout.setOnClickListener {
                var intent = Intent(context, AddDiaryActivity::class.java)
                intent.putExtra("date", list.get(adapterPosition).date)
                context.startActivity(intent)
            }
//            btnMore.setOnClickListener {
//                itemClick.invoke(adapterPosition,it)
//            }
        }
    }
}