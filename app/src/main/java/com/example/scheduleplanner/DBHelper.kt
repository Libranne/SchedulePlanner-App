package com.example.scheduleplanner

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context?) : SQLiteOpenHelper(context, "Login.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table users(username TEXT primary key,password TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists users")
    }


    fun insertDate(username: String?, password: String?): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()



        values.put("username", username)
        values.put("password", password)

        val result = db.insert("users", null, values)
        return if (result == -1L) false
        else true
    }

    fun checkusername(username: String): Boolean {
        val db = this.writableDatabase
        val cursor = db.rawQuery("select * from users where username=?", arrayOf(username))
        return if (cursor.count > 0) true
        else false
    }

    fun checkusernamepassword(username: String, password: String): Boolean {
        val db = this.writableDatabase
        val cursor = db.rawQuery(
            "select * from users where username=? and password=?",
            arrayOf(username, password)
        )
        return if (cursor.count > 0) true
        else false
    }

    companion object {
        const val DBNAME: String = "Login..db"
    }
}