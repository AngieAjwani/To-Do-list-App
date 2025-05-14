package com.example.to_dolist.ui.task

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
import com.example.to_dolist.data.database.TaskDao
import com.example.to_dolist.data.model.TaskEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ToDoListFragment : Fragment(R.layout.fragment_to_do_list) {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskDao: TaskDao
    private var deletedTask: TaskEntity? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        taskRecyclerView = view.findViewById(R.id.taskRecyclerView)
        taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize adapter with empty list
        taskAdapter = TaskAdapter(emptyList(), object : TaskAdapter.OnItemLongClickListener {
            override fun onItemLongClick(task: TaskEntity) {
                lifecycleScope.launch {
                    taskDao.deleteTask(task)
                    deletedTask = task
                    Snackbar.make(view, "Task deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo") {
                            lifecycleScope.launch {
                                deletedTask?.let { taskDao.insertTask(it) }
                            }
                        }.show()
                }
            }
        },
            clickListener = object : TaskAdapter.OnItemClickListener {
            override fun onItemClick(task: TaskEntity) {
                val bundle = Bundle().apply {
                    putInt("taskId", task.id)
                    putString("title", task.title)
                    putString("description", task.description)
                }
                findNavController().navigate(
                    R.id.action_toDoListFragment_to_addEditTaskFragment,
                    bundle
                )
            }
        }
        )

        taskRecyclerView.adapter = taskAdapter

        // Get instance of TaskDao from Room Database
        val appDatabase = AppDatabase.getDatabase(requireContext()) // Assuming you have a Room database instance
        taskDao = appDatabase.taskDao()

        taskDao.getAllTasks().observe(viewLifecycleOwner) { tasks ->
            taskAdapter.updateData(tasks)
        }

        // FAB to navigate to AddEditTaskFragment
        val addTaskFab = view.findViewById<FloatingActionButton>(R.id.addTaskFab)
        addTaskFab?.setOnClickListener {
            findNavController().navigate(R.id.action_toDoListFragment_to_addEditTaskFragment)
        }
    }
}