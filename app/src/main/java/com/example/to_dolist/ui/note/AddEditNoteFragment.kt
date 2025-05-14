package com.example.to_dolist.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.to_dolist.R
import com.example.to_dolist.data.database.AppDatabase
import com.example.to_dolist.data.database.NoteDao
import com.example.to_dolist.data.model.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddEditNoteFragment : Fragment(R.layout.fragment_add_edit_note) {

    private lateinit var noteDao: NoteDao
    private var noteId: Int? = null // Used to determine if we're editing

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleEditText = view.findViewById<EditText>(R.id.titleEditText)
        val contentEditText = view.findViewById<EditText>(R.id.contentEditText)
        val saveButton = view.findViewById<Button>(R.id.saveNoteButton)

        // Retrieve arguments
        noteId = arguments?.getInt("noteId")
        val title = arguments?.getString("title")
        val content = arguments?.getString("content")

        if (title != null && content != null) {
            titleEditText.setText(title)
            contentEditText.setText(content)
        }

        noteDao = AppDatabase.getDatabase(requireContext()).noteDao()

        saveButton.setOnClickListener {
            val newTitle = titleEditText.text.toString().trim()
            val newContent = contentEditText.text.toString().trim()

            if (newTitle.isNotEmpty() && newContent.isNotEmpty()) {
                val note = NoteEntity(
                    id = noteId ?: 0, // If editing, ID will be preserved
                    title = newTitle,
                    content = newContent,
                    timestamp = System.currentTimeMillis()
                )

                lifecycleScope.launch {
                    noteDao.insertNote(note) // Insert will replace if ID exists
                    findNavController().navigateUp()
                }
            } else {
                Toast.makeText(requireContext(), "Title and content cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
