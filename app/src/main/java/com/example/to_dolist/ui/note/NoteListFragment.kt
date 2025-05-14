package com.example.to_dolist.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.to_dolist.R
import com.example.to_dolist.data.database.AppDatabase
import com.example.to_dolist.data.database.NoteDao
import com.example.to_dolist.data.model.NoteEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class NoteListFragment : Fragment(R.layout.fragment_note_list), NoteAdapter.NoteItemListener {

    private lateinit var noteDao: NoteDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private var deletedNote: NoteEntity? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteDao = AppDatabase.getDatabase(requireContext()).noteDao()
        recyclerView = view.findViewById(R.id.noteRecyclerView)
        noteAdapter = NoteAdapter(emptyList(), this)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = noteAdapter

        // Observe notes from database
        noteDao.getAllNotes().observe(viewLifecycleOwner) { notes ->
            noteAdapter.updateNotes(notes)
        }

        // Optional: Add button to navigate to AddEditNoteFragment
        view.findViewById<FloatingActionButton?>(R.id.addNoteFab)?.setOnClickListener {
            findNavController().navigate(R.id.action_noteListFragment_to_addEditNoteFragment)
        }
    }

    override fun onNoteClicked(note: NoteEntity) {
        val bundle = Bundle().apply {
            putInt("noteId", note.id)
            putString("title", note.title)
            putString("content", note.content)
        }
        findNavController().navigate(
            R.id.action_noteListFragment_to_addEditNoteFragment,
            bundle
        )
    }

    override fun onNoteLongClicked(note: NoteEntity) {
        lifecycleScope.launch {
            noteDao.deleteNote(note)
            deletedNote = note
            Snackbar.make(requireView(), "Note deleted", Snackbar.LENGTH_LONG)
                .setAction("Undo") {
                    lifecycleScope.launch {
                        deletedNote?.let { noteDao.insertNote(it) }
                    }
                }.show()
        }
    }
}
