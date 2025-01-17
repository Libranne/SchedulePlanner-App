package com.example.scheduleplanner.Database

import java.io.Serializable

data class Note(
    var id: Int = 0,            // ID của ghi chú
    var userId: Int = 0,        // ID của người dùng liên kết với ghi chú
    var tieuDe: String = "",    // Tiêu đề của ghi chú
    var noiDung: String = "",   // Nội dung của ghi chú
    var ngayTao: String = ""    // Ngày tạo của ghi chú
) : Serializable {

    // Constructor không có ID (sử dụng khi thêm mới ghi chú)
    constructor(userId: Int, tieuDe: String, noiDung: String, ngayTao: String) : this(
        id = 0,
        userId = userId,
        tieuDe = tieuDe,
        noiDung = noiDung,
        ngayTao = ngayTao
    )
}
