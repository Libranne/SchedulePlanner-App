package com.example.scheduleplanner

import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent

class DetailNoteActivity : AppCompatActivity() {

    private lateinit var tvTieuDe: TextView
    private lateinit var tvNoiDung: TextView
    private lateinit var tvNgayTao: TextView
    private lateinit var btnEdit: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_note)

        // Get views
        tvTieuDe = findViewById(R.id.tvTieuDe)
        tvNoiDung = findViewById(R.id.tvNoiDung)
        tvNgayTao = findViewById(R.id.tvNgayTao)
        btnEdit = findViewById(R.id.EditBtn)
        btnDelete = findViewById(R.id.DeleteBtn)

        // Receive data from Intent
        val note = intent.getSerializableExtra("note") as? Note

        // Set data to TextViews
        note?.let {
            tvTieuDe.text = it.tieuDe
            tvNoiDung.text = it.noiDung
            tvNgayTao.text = it.ngayTao
        }

        // Edit button click listener
        btnEdit.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            intent.putExtra("action", "update")
            intent.putExtra("note", note)
            startActivity(intent)
        }

        // Delete button click listener
        btnDelete.setOnClickListener {
            note?.let {
                // Implement logic to delete the note from your data source (e.g., database or list)
                Toast.makeText(this, "Đã xóa ghi chú: ${it.tieuDe}", Toast.LENGTH_SHORT).show()
                // Optionally, navigate back to a previous screen
                finish()
            } ?: run {
                Toast.makeText(this, "Không tìm thấy ghi chú để xóa", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
