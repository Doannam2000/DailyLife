package dd.wan.dailylife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.fragment.app.*
import dd.wan.dailylife.fragment.CalendarFrag
import dd.wan.dailylife.fragment.SettingFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : AppCompatActivity() {
//    var check = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        navMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        var listFragment = ArrayList<Fragment>()
        listFragment.add(DialogFragment())
        listFragment.add(CalendarFrag())
        listFragment.add(SettingFragment())
        var adapter = ViewAdapter(supportFragmentManager, 0, listFragment)
        content.adapter = adapter
        tabLayout.setupWithViewPager(content)
        fbtnAdd.setOnClickListener { startActivity(Intent(this,AddDiaryActivity::class.java)) }
    }

    class ViewAdapter(fm: FragmentManager, behavior: Int, var list: ArrayList<Fragment>) :
        FragmentPagerAdapter(fm, behavior) {
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
                else ->return "Gần đây"
            }
        }

    }


//    rq pass if tab another app
//
//    override fun onResume() {
//        super.onResume()
//        if(!check)
//        {
//            check = true
//            startActivity(Intent(this,PassActivity::class.java))
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        check = false
//    }
}

