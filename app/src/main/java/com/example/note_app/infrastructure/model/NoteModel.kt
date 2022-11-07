package com.example.note_app.infrastructure.model

import androidx.room.Entity
import com.example.note_app.domain.entities.Note
import java.time.LocalDateTime

@Entity
class NoteModel(title: String, content: String, date: LocalDateTime, color: Int, id: Int? = null)
    : Note(title,content, date, color, id) {
}