package com.radikmih.noteapp.feature_note.domain.use_case

data class NoteUseCases(
    val getAllNotesUseCase: GetAllNotesUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase,
    val addNoteUseCase: AddNoteUseCase,
    val getNoteByIdUseCase: GetNoteByIdUseCase
)
