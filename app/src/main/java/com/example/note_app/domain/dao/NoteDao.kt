package com.example.note_app.domain.dao
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.note_app.core.constants.DatabaseName
import com.example.note_app.core.databases.AppDatabase
import com.example.note_app.infrastructure.model.NoteModel

@Dao
interface NoteDao {

    @Query("SELECT * FROM NoteModel")
    fun getNotes(): List<NoteModel>

    @Query("SELECT * FROM NoteModel WHERE id = :id")
    fun getNote(id: Int): NoteModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: NoteModel)

    @Delete
    fun deleteNote(note: NoteModel)


    companion object {
        private var dao: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if(dao != null) return dao!!

            dao = Room.databaseBuilder(
                context,
                AppDatabase::class.java, DatabaseName
            ).build()

            return dao!!
        }
    }
}
