package com.example.note_app.infrastructure.repositories
import androidx.lifecycle.LiveData
import com.example.note_app.domain.dao.NoteDao
import com.example.note_app.infrastructure.model.NoteModel
import com.example.note_app.domain.repositories.INoteRepository

class NoteRepository(private val dao: NoteDao) : INoteRepository {

    override fun getNotes(): List<NoteModel> {
        return dao.getNotes()
    }

    override fun getNote(id: Int): NoteModel? {
        return dao.getNote(id)
    }

    override fun insertNote(note: NoteModel) {
        dao.insertNote(note)
    }

    override fun deleteNote(note: NoteModel) {
        dao.deleteNote(note)

    }
}



