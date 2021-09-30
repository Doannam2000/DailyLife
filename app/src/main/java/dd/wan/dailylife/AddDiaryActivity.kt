package dd.wan.dailylife

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import dd.wan.dailylife.model.Note
import kotlinx.android.synthetic.main.activity_add_diary.*
import java.text.SimpleDateFormat
import java.util.*

class AddDiaryActivity : AppCompatActivity() {
    val sdf = SimpleDateFormat("dd/MM/yyyy")

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_diary)
        btnBack.setOnClickListener { finish() }
        supportActionBar?.hide()
        var cal = Calendar.getInstance()
        var sqlHelper = SQLHelper(this)
        // lấy dữ liệu
        var intent = getIntent()
        var date = intent.extras?.get("date")
        if (date != null)
            cal.time = date as Date

        var nt = checkDate(cal.time)
        txtDate.text = sdf.format(nt.date)
        editTitle.setText(nt.title)
        editContent.setText(nt.string)


        // chọn ngày viết ghi chú
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                var nt = checkDate(cal.time)
                txtDate.text = sdf.format(nt.date)
                editTitle.setText(nt.title)
                editContent.setText(nt.string)
            }
        txtDate.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        btnSave.setOnClickListener {
            if (editTitle.text.equals("") || editContent.text.equals("")) {
            } else {
                var date = cal.time
                if (sqlHelper.checkInsert(Note(date, editTitle.text.toString(), editContent.text.toString())) > -1) {
                    Toast.makeText(this,"Ghi chú đã được lưu lại",Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(this,"Lưu không thành công ",Toast.LENGTH_SHORT).show()
                }
                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun checkDate(date: Date): Note {
        val sqlHelper = SQLHelper(this)
        var list = sqlHelper.getAllDB()
        for (item in list) {
            if (sdf.format(date).equals(sdf.format(item.date)))
                return item
        }
        return Note(date, "", "")
    }
}