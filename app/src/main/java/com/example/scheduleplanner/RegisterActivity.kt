package com.example.scheduleplanner

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var regEmail: EditText
    private lateinit var regPwd: EditText
    private lateinit var edtRepass: EditText
    private lateinit var tvLabel: TextView
    private lateinit var tvHaveacc: TextView
    private lateinit var btnRegis: Button
    private lateinit var dbHelper: DBHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_register)

        // Ánh xạ các view
        regEmail = findViewById(R.id.edtRegEmail)
        regPwd = findViewById(R.id.edtPassword)
        edtRepass = findViewById(R.id.edtRepass)
        tvLabel = findViewById(R.id.tvLabel)
        tvHaveacc = findViewById(R.id.tvHaveacc)
        btnRegis = findViewById(R.id.btnLogin)
        dbHelper = DBHelper(this)

        // Xử lý khi nhấn vào "Đã có tài khoản"
        tvHaveacc.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        // Xử lý khi nhấn nút đăng ký
        btnRegis.setOnClickListener {
            val user = regEmail.text.toString()
            val pass = regPwd.text.toString()
            val repass = edtRepass.text.toString()

            if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(repass)) {
                Toast.makeText(this@RegisterActivity, "Yêu cầu nhập", Toast.LENGTH_SHORT).show()
            } else {
                if (pass == repass) {
                    val checkUser = dbHelper.checkusername(user)
                    if (!checkUser) {
                        val insert = dbHelper.insertDate(user, pass)
                        if (insert) {
                            Toast.makeText(this@RegisterActivity, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@RegisterActivity, "Đăng ký thất bại", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@RegisterActivity, "User đã tồn tại", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, "Mật khẩu không phù hợp", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
