package com.example.scheduleplanner

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.scheduleplanner.Database.DBHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var loginEmail: EditText
    private lateinit var loginPwd: EditText
    private lateinit var tvLabel: TextView
    private lateinit var tvTaiKhoan: TextView
    private lateinit var tvMatKhau: TextView
    private lateinit var tvDangKy: TextView
    private lateinit var btnLogin: Button
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_login)

        // Khởi tạo các view
        loginEmail = findViewById(R.id.edtRegEmail)
        loginPwd = findViewById(R.id.edtPassword)
        tvLabel = findViewById(R.id.tvLabel)
        tvTaiKhoan = findViewById(R.id.tvTaiKhoan)
        tvMatKhau = findViewById(R.id.tvMatKhau)
        tvDangKy = findViewById(R.id.tvDangKy)
        btnLogin = findViewById(R.id.btnLogin)
        db = DBHelper(this)

        // Xử lý sự kiện click vào TextView "Đăng Ký"
        tvDangKy.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Xử lý sự kiện click vào Button "Đăng Nhập"
        btnLogin.setOnClickListener {
            val user = loginEmail.text.toString()
            val pass = loginPwd.text.toString()

            if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
                Toast.makeText(this, "Yêu cầu nhập", Toast.LENGTH_SHORT).show()
            } else {
                val checkUserPass = db.checkUsernamePassword(user, pass)
                if (checkUserPass) {
                    Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
