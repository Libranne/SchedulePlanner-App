package com.example.scheduleplanner.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Tạo bảng users
        db.execSQL("CREATE TABLE users(username TEXT PRIMARY KEY, password TEXT)")

        // Tạo bảng tblNote
        db.execSQL(
            "CREATE TABLE tblNote (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "tieuDe TEXT, " +
                    "noiDung TEXT, " +
                    "ngayTao TEXT, " +
                    "userId INTEGER, " +
                    "FOREIGN KEY(userId) REFERENCES users(rowid))"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Thay đổi cấu trúc bảng khi nâng cấp phiên bản (giữ dữ liệu cũ nếu cần)
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS tblNote")
        onCreate(db)
    }

    // Thêm tài khoản người dùng
    fun insertUser(username: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put("username", username)
        values.put("password", password)
        return db.insert("users", null, values) != -1L
    }

    // Kiểm tra username đã tồn tại
    fun checkUsername(username: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE username=?", arrayOf(username))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // Kiểm tra tài khoản và mật khẩu
    fun checkUsernamePassword(username: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE username=? AND password=?",
            arrayOf(username, password)
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // Lấy userId từ tài khoản và mật khẩu
    fun getUserId(username: String, password: String): Int? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT rowid FROM users WHERE username=? AND password=?",
            arrayOf(username, password)
        )
        var userId: Int? = null
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("rowid"))
        }
        cursor.close()
        return userId
    }

    // Thêm ghi chú
    fun insertNote(tieuDe: String, noiDung: String, ngayTao: String, userId: Int): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put("tieuDe", tieuDe)
        values.put("noiDung", noiDung)
        values.put("ngayTao", ngayTao)
        values.put("userId", userId)
        return db.insert("tblNote", null, values) != -1L
    }

    // Lấy danh sách ghi chú của userId
    fun getNotesByUserId(userId: Int): ArrayList<Map<String, String>> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tblNote WHERE userId=?", arrayOf(userId.toString()))
        val notes = ArrayList<Map<String, String>>()
        if (cursor.moveToFirst()) {
            do {
                val note = mapOf(
                    "id" to cursor.getInt(0).toString(),
                    "tieuDe" to cursor.getString(1),
                    "noiDung" to cursor.getString(2),
                    "ngayTao" to cursor.getString(3)
                )
                notes.add(note)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return notes
    }

    companion object {
        const val DB_NAME = "SchedulePlanner.db"
        const val DB_VERSION = 1
    }
    fun updateNote(note: Note): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put("tieuDe", note.tieuDe)
        values.put("noiDung", note.noiDung)
        values.put("ngayTao", note.ngayTao)
        return db.update("tblNote", values, "id=?", arrayOf(note.id.toString())) > 0
    }

}
