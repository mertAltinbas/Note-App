package com.example.note_app.core.databases
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.note_app.core.databases.converters.LocalDateTimeConverter
import com.example.note_app.infrastructure.model.NoteModel
import com.example.note_app.domain.dao.NoteDao

@TypeConverters(LocalDateTimeConverter::class)
@Database(entities = [NoteModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}