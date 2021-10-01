package dd.wan.dailylife.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import dd.wan.dailylife.*
import dd.wan.dailylife.model.WriteReadFile
import kotlinx.android.synthetic.main.fragment_setting.view.*


class SettingFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.P)
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
        val writeReadFile = WriteReadFile(requireContext())
        val sqlHelper = SQLHelper(context)
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
        view.btnBackUp.setOnClickListener {
            writeReadFile.writeFile(sqlHelper.getAllDB())
            Toast.makeText(context,"Đã sao lưu thành công", Toast.LENGTH_SHORT).show()
        }
        view.btnRestore.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setMessage("Khi khôi phục dữ liệu hiện tại sẽ mất bạn có muốn tiếp tục ?")
            builder.setCancelable(true)
            builder.setPositiveButton("Có") { dialog, id ->
                sqlHelper.deleteAllDB()
                var list = writeReadFile.readFile()
                sqlHelper.insertList(list)
                startActivity(
                    Intent(
                        context,
                        MainActivity::class.java,
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK),
                )
                Toast.makeText(context,"Đã khôi phục thành công", Toast.LENGTH_SHORT).show()
                dialog.cancel()
            }
            builder.setNegativeButton("Không") { dialog, id -> dialog.cancel() }
            val alert: AlertDialog = builder.create()
            alert.show()
        }
        return view
    }

}