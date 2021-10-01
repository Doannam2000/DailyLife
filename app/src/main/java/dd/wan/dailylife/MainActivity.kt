package dd.wan.dailylife

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.fragment.app.*
import dd.wan.dailylife.fragment.CalendarFrag
import dd.wan.dailylife.fragment.DiaryFragment
import dd.wan.dailylife.fragment.SettingFragment
import dd.wan.dailylife.model.Note
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
                    writeFile(sqlHelper.getAllDB())
                }
                R.id.restore-> {
                    sqlHelper.deleteAllDB()
                    var list = readFile()
                    sqlHelper.insertList(list)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun writeFile(list: ArrayList<Note>) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            val filePaths: String = filesDir.parentFile.toString() + "/diary.csv"
            Log.d("filepath", filePaths)
            val file = File(filePaths)
            val writer = Files.newBufferedWriter(Paths.get(file.toURI()))
            val csvPrinter =
                CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Date", "Title", "Content"))
            for (item in list) {
                csvPrinter.printRecord(sdf.format(item.date), item.title, item.string)
            }
            csvPrinter.flush()
            csvPrinter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun readFile(): ArrayList<Note> {
        var list = ArrayList<Note>()
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val filePaths: String = filesDir.parentFile.toString() + "/diary.csv"
        val file = File(filePaths)
        if (file.exists())
            try {
                val reader = Files.newBufferedReader(Paths.get(file.toURI()))
                val records: Iterable<CSVRecord> = CSVFormat.EXCEL.withHeader().parse(reader)
                for (item in records) {
                    val date = sdf.parse(item.get("Date"))
                    val title = item.get("Title")
                    val content = item.get("Content")
                    list.add(Note(date, title, content))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        return list
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

