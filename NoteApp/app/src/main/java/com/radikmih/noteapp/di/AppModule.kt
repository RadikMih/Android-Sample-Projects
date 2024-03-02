package com.radikmih.noteapp.di

import android.app.Application
import androidx.room.Room
import com.radikmih.noteapp.feature_note.data.data_source.NoteDatabase
import com.radikmih.noteapp.feature_note.data.repository.NoteRepositoryImpl
import com.radikmih.noteapp.feature_note.domain.repository.NoteRepository
import com.radikmih.noteapp.feature_note.domain.use_case.AddNoteUseCase
import com.radikmih.noteapp.feature_note.domain.use_case.DeleteNoteUseCase
import com.radikmih.noteapp.feature_note.domain.use_case.GetAllNotesUseCase
import com.radikmih.noteapp.feature_note.domain.use_case.GetNoteByIdUseCase
import com.radikmih.noteapp.feature_note.domain.use_case.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getAllNotesUseCase = GetAllNotesUseCase(repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository),
            addNoteUseCase = AddNoteUseCase(repository),
            getNoteByIdUseCase = GetNoteByIdUseCase(repository)
        )
    }
}