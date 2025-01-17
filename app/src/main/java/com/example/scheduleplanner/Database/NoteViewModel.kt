package com.example.scheduleplanner.Database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.scheduleplanner.NoteProvider

class NoteViewModel : ViewModel() {

    private val _notesLiveData = MutableLiveData<List<Note>>()
    val notesLiveData: LiveData<List<Note>> get() = _notesLiveData

    private val notesList = ArrayList<Note>()

    fun getNotes(noteProvider: NoteProvider) {
        val notes = noteProvider.getAllNote()
        notesList.clear()
        notesList.addAll(notes)
        _notesLiveData.value = notesList
    }

    fun addNote(note: Note) {
        notesList.add(note)
        _notesLiveData.value = notesList
    }

    fun updateNote(updatedNote: Note) {
        val index = notesList.indexOfFirst { it.id == updatedNote.id }
        if (index != -1) {
            notesList[index] = updatedNote
            _notesLiveData.value = notesList
        }
    }

    fun deleteNote(note: Note) {
        notesList.remove(note)
        _notesLiveData.value = notesList
    }
}
