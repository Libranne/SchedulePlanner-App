package com.example.scheduleplanner

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.Cursor

class NoteProvider(private val context: Context) {
    private val openHelper: DbOpenHelper = DbOpenHelper(context)  // Khởi tạo đối tượng DbOpenHelper
    private lateinit var db: SQLiteDatabase // Đối tượng SQLiteDatabase để thao tác với CSDL

    companion object {
        const val TB_NAME = "tblNote"
        const val _ID = "id"
        const val TIEU_DE = "tieuDe"
        const val NOI_DUNG = "noiDung"
        const val NGAY_TAO = "ngayTao"
    }

    // Phương thức để thêm một ghi chú vào CSDL
    fun insNote(note: Note): Boolean {
        db = openHelper.writableDatabase  // Lấy đối tượng CSDL với quyền ghi
        val values = ContentValues().apply {
            put(TIEU_DE, note.tieuDe)
            put(NOI_DUNG, note.noiDung)
            put(NGAY_TAO, note.ngayTao)
        }

        // Thực hiện câu lệnh INSERT
        return db.insert(TB_NAME, null, values) != -1L
    }

    // Phương thức để cập nhật ghi chú
    fun updNote(note: Note, tieuDe: String): Int {
        db = openHelper.writableDatabase  // Lấy đối tượng CSDL với quyền ghi
        val values = ContentValues().apply {
            put(NOI_DUNG, note.noiDung)
            put(NGAY_TAO, note.ngayTao)
        }

        val whereArgs = arrayOf(tieuDe)  // Điều kiện để cập nhật
        return db.update(TB_NAME, values, "$TIEU_DE = ?", whereArgs)
    }

    // Phương thức để xóa ghi chú
    fun delNote(id: String): Int {
        db = openHelper.writableDatabase  // Lấy đối tượng CSDL với quyền ghi
        return db.delete(TB_NAME, "$_ID = ?", arrayOf(id))
    }

    // Phương thức để lấy tất cả ghi chú
    fun getAllNote(): ArrayList<Note> {
        val result = ArrayList<Note>()
        db = openHelper.readableDatabase  // Lấy đối tượng CSDL với quyền đọc
        val sql = "SELECT * FROM $TB_NAME"
        val cursor: Cursor = db.rawQuery(sql, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val tieuDe = cursor.getString(1)
            val noiDung = cursor.getString(2)
            val ngayTao = cursor.getString(3)
            val note = Note(id, tieuDe, noiDung, ngayTao)
            result.add(note)
        }

        cursor.close()  // Đảm bảo đóng cursor sau khi xong
        return result
    }
}
