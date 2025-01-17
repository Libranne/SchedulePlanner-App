package com.example.scheduleplanner

import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.widget.Toast
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.scheduleplanner.Database.Note

class DetailNoteActivity : AppCompatActivity() {

    private lateinit var tvTieuDe: TextView
    private lateinit var tvNoiDung: TextView
    private lateinit var tvNgayTao: TextView
    private lateinit var btnEdit: Button
    private lateinit var btnDelete: Button
    private lateinit var noteProvider: NoteProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_note)

        // Initialize views
        tvTieuDe = findViewById(R.id.tvTieuDe)
        tvNoiDung = findViewById(R.id.tvNoiDung)
        tvNgayTao = findViewById(R.id.tvNgayTao)
        btnEdit = findViewById(R.id.EditBtn)
        btnDelete = findViewById(R.id.DeleteBtn)

        noteProvider = NoteProvider(this)

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
            note?.let {
                val intent = Intent(this, AddNoteActivity::class.java)
                intent.putExtra("action", "update")
                intent.putExtra("note", it)
                startActivity(intent)
            }
        }

        // Delete button click listener
        btnDelete.setOnClickListener {
            note?.let {
                showDeleteConfirmationDialog(it)
            }
        }
    }

    /**
     * Show delete confirmation dialog, similar to the NoteAdapter
     */
    private fun showDeleteConfirmationDialog(note: Note) {
        AlertDialog.Builder(this)
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete the note titled '${note.tieuDe}'?")
            .setPositiveButton("Yes") { _, _ ->
                deleteNoteFromDatabase(note)
            }
            .setNegativeButton("No", null)
            .show()
    }

    /**
     * Handle the deletion of the note from the database and update UI accordingly
     */
    private fun deleteNoteFromDatabase(note: Note) {
        val isDeleted = noteProvider.delNote(note.id.toString()) > 0
        if (isDeleted) {
            Toast.makeText(this, "Note deleted: ${note.tieuDe}", Toast.LENGTH_SHORT).show()
            finish() // Close activity and return to previous screen
        } else {
            Toast.makeText(this, "Could not delete the note", Toast.LENGTH_SHORT).show()
        }
    }
}
