package com.example.to_dolist.ui.task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.to_dolist.R
import com.example.to_dolist.data.database.AppDatabase
import com.example.to_dolist.data.database.TaskDao
import com.example.to_dolist.data.model.TaskEntity
import kotlinx.coroutines.launch

class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    private lateinit var taskDao: TaskDao
    private var taskId: Int? = null // <-- Use this to distinguish between new/edit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleEditText = view.findViewById<EditText>(R.id.editTextTaskTitle)
        val descriptionEditText = view.findViewById<EditText>(R.id.editTextTaskDescription)
        val saveButton = view.findViewById<Button>(R.id.buttonSaveTask)

        taskId = arguments?.getInt("taskId")
        val title = arguments?.getString("title")
        val description = arguments?.getString("description")

        if (title != null && description != null) {
            titleEditText.setText(title)
            descriptionEditText.setText(description)
        }

        taskDao = AppDatabase.getDatabase(requireContext()).taskDao()

        saveButton.setOnClickListener {
            val newTitle = titleEditText.text.toString().trim()
            val newDescription = descriptionEditText.text.toString().trim()

            if (newTitle.isNotEmpty()) {
                val task = TaskEntity(
                    id = taskId ?: 0, // <-- This ensures Room replaces if editing
                    title = newTitle,
                    description = newDescription,
                    timestamp = System.currentTimeMillis()
                )

                lifecycleScope.launch {
                    taskDao.insertTask(task) // Room will update if id exists
                    findNavController().navigateUp()
                }
            } else {
                Toast.makeText(requireContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

