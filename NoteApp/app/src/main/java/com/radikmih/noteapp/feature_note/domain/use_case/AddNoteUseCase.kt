package com.radikmih.noteapp.feature_note.domain.use_case

import com.radikmih.noteapp.feature_note.domain.model.InvalidNoteException
import com.radikmih.noteapp.feature_note.domain.model.Note
import com.radikmih.noteapp.feature_note.domain.repository.NoteRepository

class AddNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {

        @Throws(InvalidNoteException::class)
        if (note.title.isBlank()) {
            throw InvalidNoteException("The title of the note can't be empty")
        }

        @Throws(InvalidNoteException::class)
        if (note.content.isBlank()) {
            throw InvalidNoteException("The content of the note can't be empty")
        }

        repository.insertNote(note)
    }
}