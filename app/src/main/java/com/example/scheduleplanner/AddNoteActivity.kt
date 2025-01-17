package com.example.scheduleplanner

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.scheduleplanner.Database.Note
import java.util.*

class AddNoteActivity : AppCompatActivity() {

    private lateinit var etTieuDe: EditText
    private lateinit var etNoiDung: EditText
    private lateinit var etNgayTao: EditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private var msg = ""
    private var userId = 0  // Thêm biến userId để lưu thông tin người dùng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        getViews()

        // Nhận dữ liệu từ Intent
        val intentReceipt = intent
        msg = intentReceipt.getStringExtra("action") ?: ""
        userId = intentReceipt.getIntExtra("userId", 0) // Nhận userId từ Intent

        if (msg == "update") {
            // Set dữ liệu lên các trường EditText nếu là chế độ update
            val note = intentReceipt.getSerializableExtra("note") as? Note
            note?.let {
                etTieuDe.setText(it.tieuDe)
                etNoiDung.setText(it.noiDung)
                etNgayTao.setText(it.ngayTao)
            }
        }

        // Xử lý nút Lưu
        btnSave.setOnClickListener { onSaveNote() }

        // Xử lý nút Hủy
        btnCancel.setOnClickListener { finish() }

        // Chọn ngày khi nhấn vào trường ngày tạo
        etNgayTao.setOnClickListener { onSelectDate() }
    }

    // Xử lý lưu ghi chú
    private fun onSaveNote() {
        val note = getNote()
        val intentResult = Intent().apply {
            putExtra("action", msg)
            putExtra("note", note)
        }
        setResult(RESULT_OK, intentResult)
        finish()
    }

    // Lấy các View từ layout
    private fun getViews() {
        etTieuDe = findViewById(R.id.etTieuDe)
        etNoiDung = findViewById(R.id.etNoiDung)
        etNgayTao = findViewById(R.id.etNgayTao)
        btnSave = findViewById(R.id.SaveBtn)
        btnCancel = findViewById(R.id.CancelBtn)
    }

    // Lấy đối tượng Note từ các trường input
    private fun getNote(): Note {
        val tieuDe = etTieuDe.text.toString().trim()
        val noiDung = etNoiDung.text.toString().trim()
        val ngayTao = etNgayTao.text.toString().trim()
        return Note(userId, tieuDe, noiDung, ngayTao)  // Thêm userId vào note
    }

    // Mở DatePicker để chọn ngày
    private fun onSelectDate() {
        val c = Calendar.getInstance()
        val ngay = c.get(Calendar.DAY_OF_MONTH)
        val thang = c.get(Calendar.MONTH)
        val nam = c.get(Calendar.YEAR)

        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            etNgayTao.setText(String.format("%02d/%02d/%d", dayOfMonth, month + 1, year))
        }, nam, thang, ngay)

        datePickerDialog.show()
    }
}
