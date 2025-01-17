package com.example.scheduleplanner

import android.content.ContentValues
import android.content.Context
import com.example.scheduleplanner.Database.DBHelper
import com.example.scheduleplanner.Database.Note

class NoteProvider(private val context: Context) {
    private val dbHelper: DBHelper = DBHelper(context) // Sử dụng DBHelper
    private val TB_NAME = "tblNote"

    // Phương thức để thêm một ghi chú vào CSDL
    fun insNote(note: Note): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("tieuDe", note.tieuDe)
            put("noiDung", note.noiDung)
            put("ngayTao", note.ngayTao)
        }

        val result = db.insert(TB_NAME, null, values) != -1L
        db.close()
        return result
    }

    // Phương thức để cập nhật ghi chú
    fun updNote(note: Note): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("tieuDe", note.tieuDe)
            put("noiDung", note.noiDung)
            put("ngayTao", note.ngayTao)
        }

        val whereArgs = arrayOf(note.id.toString())
        val rowsUpdated = db.update(TB_NAME, values, "id = ?", whereArgs)
        db.close()
        return rowsUpdated
    }

    // Phương thức để xóa ghi chú
    fun delNote(id: String): Int {
        return try {
            val db = dbHelper.writableDatabase
            val rowsDeleted = db.delete(TB_NAME, "id = ?", arrayOf(id))
            db.close()
            rowsDeleted
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    // Phương thức để lấy tất cả ghi chú
    fun getAllNote(): ArrayList<Note> {
        val result = ArrayList<Note>()
        val db = dbHelper.readableDatabase
        val sql = "SELECT * FROM $TB_NAME"
        db.rawQuery(sql, null).use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val tieuDe = cursor.getString(cursor.getColumnIndexOrThrow("tieuDe"))
                val noiDung = cursor.getString(cursor.getColumnIndexOrThrow("noiDung"))
                val ngayTao = cursor.getString(cursor.getColumnIndexOrThrow("ngayTao"))
                result.add(Note(id, tieuDe, noiDung, ngayTao))
            }
        }
        db.close()
        return result
    }
}
