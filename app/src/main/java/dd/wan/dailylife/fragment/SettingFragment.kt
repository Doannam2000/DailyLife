package dd.wan.dailylife.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import dd.wan.dailylife.AddDiaryActivity
import dd.wan.dailylife.PassActivity
import dd.wan.dailylife.R


class SettingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_setting, container, false)
        var btnPass:TextView = view.findViewById(R.id.btnPass)
        val sharedPreferences = context?.getSharedPreferences("SHARE_PREFERENCES", Context.MODE_PRIVATE)
        var pin = sharedPreferences?.getInt("pin",0)
        if(pin != 0 )
        {
            btnPass.setText("Đổi mật khẩu")
        }
        btnPass.setOnClickListener {
            if(pin !=0)
            {
                var intent = Intent(context,PassActivity::class.java)
                intent.putExtra("pin",1)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
            }
            else
            {
                var intent = Intent(context,PassActivity::class.java)
                intent.putExtra("pin",2)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK))
            }
        }
        return view
    }

}