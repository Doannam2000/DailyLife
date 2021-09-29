package dd.wan.dailylife

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dd.wan.dailylife.R
import kotlinx.android.synthetic.main.activity_add_diary.*
import java.text.SimpleDateFormat
import java.util.*

class AddDiaryActivity : AppCompatActivity() {
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_diary)
        btnBack.setOnClickListener { finish() }
        supportActionBar?.hide()
        var intent = intent
        var date = intent.getSerializableExtra("date")
        if (date == null)
            date = Calendar.getInstance()
        date = date as Calendar
        txtDate.text = sdf.format(date.time) + "▼"
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            date.set(Calendar.YEAR, year)
            date.set(Calendar.MONTH, monthOfYear)
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            txtDate.text = sdf.format(date.time) + "▼"
        }
        txtDate.setOnClickListener {
            DatePickerDialog(this,dateSetListener,date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH)).show()
        }
    }
}