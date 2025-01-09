package com.example.scheduleplanner

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbOpenHelper  (context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        //tao cac bang can co trong csdl
        val sql = "CREATE TABLE tblNote (id integer primary key, " +
                "tieuDe text, noiDung text, ngayTao text); "
        //thuc thi cau lenh tao bang
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS tbNote; ")
        onCreate(db)
    }

    companion object {
        const val DB_NAME: String = "ListNote.db"
        const val DB_VERSION: Int = 1
    }
}

