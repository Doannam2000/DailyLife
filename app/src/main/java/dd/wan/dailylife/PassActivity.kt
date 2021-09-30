package dd.wan.dailylife

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_pass.*

class PassActivity : AppCompatActivity() {
    var pass = ""
    var pin = 0
    var check = 0
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass)
        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences("SHARE_PREFERENCES", Context.MODE_PRIVATE)
        pin = sharedPreferences.getInt("pin", 0)
        var intent = intent
        check = intent.getIntExtra("pin", 0)
        if (pin == 0 && check != 2) {
            Toast.makeText(this, "Nên cài mật khẩu để bảo mật dữ liệu", Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }
        when (check) {
            1 -> textView.text = "Nhập mật khẩu cũ"
            2 -> textView.text = "Nhập mật khẩu mới"
        }
        btn0.setOnClickListener(listener)
        btn1.setOnClickListener(listener)
        btn2.setOnClickListener(listener)
        btn3.setOnClickListener(listener)
        btn4.setOnClickListener(listener)
        btn5.setOnClickListener(listener)
        btn6.setOnClickListener(listener)
        btn7.setOnClickListener(listener)
        btn8.setOnClickListener(listener)
        btn9.setOnClickListener(listener)
        btnC.setOnClickListener(listener)
    }

    val listener = View.OnClickListener { view ->
        when (view.getId()) {
            R.id.btn0 -> if (pass.length < 4) pass += 0
            R.id.btn1 -> if (pass.length < 4) pass += 1
            R.id.btn2 -> if (pass.length < 4) pass += 2
            R.id.btn3 -> if (pass.length < 4) pass += 3
            R.id.btn4 -> if (pass.length < 4) pass += 4
            R.id.btn5 -> if (pass.length < 4) pass += 5
            R.id.btn6 -> if (pass.length < 4) pass += 6
            R.id.btn7 -> if (pass.length < 4) pass += 7
            R.id.btn8 -> if (pass.length < 4) pass += 8
            R.id.btn9 -> if (pass.length < 4) pass += 9
            R.id.btnC -> if (pass.length > 0) pass = pass.substring(0, pass.length - 1)
        }
        checkImage()
    }

    fun checkImage() {
        if (pass.length == 1)
            imagePass.setImageResource(R.drawable.tim)
        else if (pass.length == 2)
            imagePass.setImageResource(R.drawable.tim2)
        else if (pass.length == 3)
            imagePass.setImageResource(R.drawable.tim3)
        else if (pass.length == 4) {
            imagePass.setImageResource(R.drawable.tim4)
            if (check == 2) {
                var edit: SharedPreferences.Editor = sharedPreferences.edit()
                edit.putInt("pin", pass.toInt())
                edit.apply()
                Toast.makeText(this, "Tạo mật khẩu thành công !", Toast.LENGTH_SHORT).show()
                startActivity(
                    Intent(
                        this,
                        MainActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            } else {
                if (pass.toInt() == pin) {
                    if (check == 1) {
                        textView.text = "Nhập mật khẩu mới"
                        check++
                        pass = ""
                        checkImage()
                    } else {
                        startActivity(
                            Intent(
                                this,
                                MainActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        )
                    }
                } else {
                    val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
                    textView.text = "Mật khẩu không đúng"
                    imagePass.animation = shake
                    val handel = Handler()
                    handel.postDelayed(Runnable {
                        textView.text = "Nhập mật khẩu nà"
                        imagePass.setImageDrawable(null)
                        pass = ""
                    }, 1000)
                }
            }
        } else
            imagePass.setImageDrawable(null)
    }
}