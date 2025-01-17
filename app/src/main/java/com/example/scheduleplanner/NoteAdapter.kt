package com.example.scheduleplanner

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import com.example.scheduleplanner.Database.Note

class NoteAdapter(
    context: Context,
    private val listNote: ArrayList<Note>,
    private val activityResultLauncher: ActivityResultLauncher<Intent>,
    private val onDeleteCallback: (Note) -> Unit // Callback for deleting the note
) : ArrayAdapter<Note>(context, 0, listNote) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val curView = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)

        val note = getItem(position)
        val tvTieuDe = curView.findViewById<TextView>(R.id.tvTieuDe)
        val tvNgayTao = curView.findViewById<TextView>(R.id.tvNgayTao)
        val imgEdit = curView.findViewById<ImageButton>(R.id.Edit)
        val imgDel = curView.findViewById<ImageButton>(R.id.Delete)
        val checkBox = curView.findViewById<CheckBox>(R.id.checkBox)

        tvTieuDe.text = note?.tieuDe
        tvNgayTao.text = note?.ngayTao

        // Sự kiện click mở chi tiết ghi chú
        tvTieuDe.setOnClickListener {
            note?.let {
                val detailIntent = Intent(context, DetailNoteActivity::class.java)
                detailIntent.putExtra("note", it)
                context.startActivity(detailIntent)
            }
        }

        // Sự kiện click chỉnh sửa ghi chú
        imgEdit.setOnClickListener {
            note?.let {
                val updIntent = Intent(context, AddNoteActivity::class.java)
                updIntent.putExtra("action", "update")
                updIntent.putExtra("note", it)
                activityResultLauncher.launch(updIntent)
            }
        }

        // Sự kiện click xóa ghi chú
        imgDel.setOnClickListener {
            note?.let {
                showDeleteConfirmationDialog(it)
            }
        }

        // Xử lý riêng sự kiện CheckBox
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                note?.let {
                    handleCheckBox(it, checkBox)
                }
            }
        }

        return curView
    }

    /**
     * Hiển thị thông báo khi người dùng nhấn vào CheckBox
     */
    private fun handleCheckBox(note: Note, checkBox: CheckBox) {
        AlertDialog.Builder(context)
            .setTitle("Xác nhận")
            .setMessage("Bạn đã hoàn thành nhiệm vụ '${note.tieuDe}'?")
            .setPositiveButton("Đồng ý") { _, _ ->
                deleteNoteFromDatabase(note)
                Toast.makeText(context, "Bạn đã hoàn thành nhiệm vụ này!", Toast.LENGTH_SHORT).show()
                checkBox.isChecked = true
            }
            .setNegativeButton("Hủy") { _, _ ->
                checkBox.isChecked = false
            }
            .show()
    }

    /**
     * Hiển thị dialog xác nhận xóa ghi chú
     */
    private fun showDeleteConfirmationDialog(note: Note) {
        AlertDialog.Builder(context)
            .setTitle("Xóa ghi chú")
            .setMessage("Bạn có chắc chắn muốn xóa ghi chú có tiêu đề '${note.tieuDe}'?")
            .setPositiveButton("Đồng ý") { _, _ ->
                deleteNoteFromDatabase(note)
                onDeleteCallback(note) // Notify the caller to remove the note from the data source
                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun deleteNoteFromDatabase(note: Note) {
        listNote.remove(note)
        notifyDataSetChanged()
    }

    // Method to update the data in the adapter
    fun updateData(newList: List<Note>) {
        listNote.clear()
        listNote.addAll(newList)
        notifyDataSetChanged()
    }
}
