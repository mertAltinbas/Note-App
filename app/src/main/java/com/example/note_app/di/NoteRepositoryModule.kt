package com.example.note_app.di

import android.app.Application
import com.example.note_app.infrastructure.repositories.NoteRepository
import com.example.note_app.domain.dao.NoteDao
import com.example.note_app.domain.repositories.INoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal object NoteRepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideNoteRepository(app: Application) : INoteRepository = NoteRepository(NoteDao.getInstance(app).noteDao())
}