package com.example.scheduleplanner

import java.io.Serializable

data class Note(
    var id: Int = 0,
    var tieuDe: String = "",
    var noiDung: String = "",
    var ngayTao: String = ""
) : Serializable {

    // Constructor mặc định
    constructor(tieuDe: String, noiDung: String, ngayTao: String) : this(0, tieuDe, noiDung, ngayTao)
}