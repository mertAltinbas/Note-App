package com.example.note_app.domain.entities

import androidx.room.PrimaryKey
import java.time.LocalDateTime


open class Note(
    val title: String,
    val content: String,
    val date: LocalDateTime,
    val color: Int,
    @PrimaryKey val id: Int?
)