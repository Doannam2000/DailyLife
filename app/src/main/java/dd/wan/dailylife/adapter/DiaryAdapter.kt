package dd.wan.dailylife.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dd.wan.dailylife.R
import dd.wan.dailylife.model.note
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DiaryAdapter(var list:ArrayList<note>) : RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {
    val sdfDate = SimpleDateFormat("dd")
    val sdfDay = SimpleDateFormat("dd/MM/yyyy")
    val sdfMonth = SimpleDateFormat("MM")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_diary, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiaryAdapter.ViewHolder, position: Int) {
        holder.setData()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var txtDay:TextView = itemView.findViewById(R.id.txtDay)
        var txtDate:TextView = itemView.findViewById(R.id.txtDate)
        var txtContent:TextView = itemView.findViewById(R.id.txtContent)
        var txtMonth:TextView = itemView.findViewById(R.id.txtMonth)
        var txtTitle:TextView = itemView.findViewById(R.id.txtTitle)
        fun setData()
        {
            var item = list.get(adapterPosition)
            var date = item.date
            txtDay.text = sdfDate.format(date)
            txtDate.text = "Ngày :"+sdfDay.format(date)
            txtMonth.text = "Th"+sdfMonth.format(date)
            txtContent.text = "✎ "+ item.string
            txtTitle.text = item.title
        }
    }
}