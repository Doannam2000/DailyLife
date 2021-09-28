package dd.wan.dailylife

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import dd.wan.dailylife.fragment.CalendarFrag
import dd.wan.dailylife.fragment.SettingFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

}

