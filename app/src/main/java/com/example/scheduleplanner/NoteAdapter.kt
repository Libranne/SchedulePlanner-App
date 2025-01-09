package com.example.scheduleplanner

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class NoteAdapter(context: Context, private val listNote: ArrayList<Note>) :
    ArrayAdapter<Note>(context, 0, listNote) {

    private val noteProvider = NoteProvider(context)

    // Đăng ký ActivityResultLauncher
    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        (context as AppCompatActivity).registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val note = result.data?.getSerializableExtra("note") as? Note
                note?.let { updateNoteInList(it) }
            }
        }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val curView = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)

        // Lấy đối tượng Note tại vị trí position
        val note = getItem(position)

        // Lấy các view trong item_note
        val tvTieuDe = curView.findViewById<TextView>(R.id.tvTieuDe)
        val tvNgayTao = curView.findViewById<TextView>(R.id.tvNgayTao)
        val imgEdit = curView.findViewById<ImageButton>(R.id.Edit)
        val imgDel = curView.findViewById<ImageButton>(R.id.Delete)

        // Gắn dữ liệu vào các view
        tvTieuDe.text = note?.tieuDe
        tvNgayTao.text = note?.ngayTao

        // Xử lý sự kiện click trên TextView Tiêu Đề để mở chi tiết ghi chú
        tvTieuDe.setOnClickListener {
            note?.let {
                // Tạo Intent để mở DetailNoteActivity và truyền dữ liệu ghi chú
                val detailIntent = Intent(context, DetailNoteActivity::class.java)
                detailIntent.putExtra("note", it)  // Truyền đối tượng Note đến Activity chi tiết
                context.startActivity(detailIntent)  // Mở Activity chi tiết
            }
        }

        // Xử lý sự kiện click trên ImageButton Edit
        imgEdit.setOnClickListener {
            note?.let {
                val updIntent = Intent(context, AddNoteActivity::class.java)
                updIntent.putExtra("action", "update")
                updIntent.putExtra("note", it)
                activityResultLauncher.launch(updIntent)
            }
        }

        // Xử lý sự kiện click trên ImageButton Delete
        imgDel.setOnClickListener {
            note?.let {
                showDeleteConfirmationDialog(it)
            }
        }

        return curView
    }

    private fun showDeleteConfirmationDialog(note: Note) {
        AlertDialog.Builder(context)
            .setTitle("Xóa ghi chú")
            .setMessage("Bạn có chắc chắn muốn xóa ghi chú có tiêu đề '${note.tieuDe}'?")
            .setPositiveButton("Đồng ý") { _, _ ->
                val isDeleted = deleteNoteFromDatabase(note)
                if (isDeleted) {
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Không thể xóa ghi chú", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun deleteNoteFromDatabase(note: Note): Boolean {
        val isDeleted = noteProvider.delNote(note.id.toString()) > 0
        if (isDeleted) {
            listNote.remove(note) // Xóa ghi chú khỏi danh sách
            notifyDataSetChanged() // Cập nhật lại giao diện
        }
        return isDeleted
    }

    private fun updateNoteInList(note: Note): Boolean {
        val index = listNote.indexOfFirst { it.id == note.id }
        return if (index != -1) {
            listNote[index] = note // Cập nhật ghi chú trong danh sách
            notifyDataSetChanged() // Cập nhật lại giao diện
            true
        } else {
            false
        }
    }
}
