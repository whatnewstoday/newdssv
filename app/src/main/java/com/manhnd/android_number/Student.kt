package com.manhnd.android_number

/**
 * Data class đại diện cho một sinh viên
 * Sử dụng data class để tự động tạo các method như equals(), hashCode(), toString(), copy()
 */
data class Student(
    var name: String,  // Họ tên sinh viên
    var mssv: String   // Mã số sinh viên
)
