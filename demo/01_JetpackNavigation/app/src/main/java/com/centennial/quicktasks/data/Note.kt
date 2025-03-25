package com.centennial.quicktasks.data
import java.time.LocalDate
import java.util.UUID

data class Note(
    var title: String,
    var content: String,
    var dueDate: String
) {
    val id: String = UUID.randomUUID().toString() // 自动生成唯一 ID
}