package com.example.midtermproject.data

data class Task(
    val id: Long,
    var title: String,
    var isCompleted: Boolean = false
)