package dd.wan.dailylife

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.fragment.app.*
import dd.wan.dailylife.fragment.CalendarFrag
import dd.wan.dailylife.fragment.DiaryFragment
import dd.wan.dailylife.fragment.SettingFragment
import dd.wan.dailylife.model.Note
import dd.wan.dailylife.model.WriteReadFile
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import java.io.File
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import org.apache.commons.csv.CSVRecord
import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.lang.System.`in`


class MainActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        var sqlHelper = SQLHelper(this)
        navMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        var writeFile = WriteReadFile(this)

        var listFragment = ArrayList<Fragment>()
        listFragment.add(DiaryFragment())
        listFragment.add(CalendarFrag())
        listFragment.add(SettingFragment())
        var adapter = ViewAdapter(supportFragmentManager, listFragment)
        content.adapter = adapter
        tabLayout.setupWithViewPager(content)
        fbtnAdd.setOnClickListener {
            startActivity(Intent(this, AddDiaryActivity::class.java))
        }
        val sharedPreferences = this.getSharedPreferences("SHARE_PREFERENCES", Context.MODE_PRIVATE)
        var pin = sharedPreferences?.getInt("pin", 0)
        if (pin != 0) {
            navigationView.menu.getItem(0).title = "Đổi mật khẩu"
        }
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.pass -> {
                    if (pin != 0) {
                        var intent = Intent(this, PassActivity::class.java)
                        intent.putExtra("pin", 1)
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
                    } else {
                        var intent = Intent(this, PassActivity::class.java)
                        intent.putExtra("pin", 2)
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
                    }
                }
                R.id.backUp -> {
                    writeFile.writeFile(sqlHelper.getAllDB())
                    Toast.makeText(this,"Đã sao lưu thành công",Toast.LENGTH_SHORT).show()
                }
                R.id.restore -> {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setMessage("Khi khôi phục dữ liệu hiện tại sẽ mất bạn có muốn tiếp tục ?")
                    builder.setCancelable(true)
                    builder.setPositiveButton("Có") { dialog, id ->
                        sqlHelper.deleteAllDB()
                        var list = writeFile.readFile()
                        sqlHelper.insertList(list)
                        startActivity(
                            Intent(
                                this,
                                MainActivity::class.java,
                            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK),
                        )
                        dialog.cancel()
                        Toast.makeText(this,"Đã khôi phục thành công", Toast.LENGTH_SHORT).show()
                    }
                    builder.setNegativeButton("Không") { dialog, id -> dialog.cancel() }
                    val alert: AlertDialog = builder.create()
                    alert.show()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }



    class ViewAdapter(fm: FragmentManager, var list: ArrayList<Fragment>) :
        FragmentPagerAdapter(fm) {
        override fun getCount(): Int {
            return list.size
        }

        override fun getItem(position: Int): Fragment {
            return list.get(position)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "Gần đây"
                1 -> return "Lịch"
                2 -> return "Thiết lập"
                else -> return "Gần đây"
            }
        }

        override fun getItemPosition(`object`: Any): Int {
            return POSITION_NONE
        }
    }

}

