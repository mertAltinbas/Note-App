package com.example.note_app.domain.repositories
import com.example.note_app.infrastructure.model.NoteModel

interface INoteRepository {
    fun getNotes(): List<NoteModel>

    fun getNote(id: Int): NoteModel?

    fun insertNote(note: NoteModel)

    fun deleteNote(note: NoteModel)
}