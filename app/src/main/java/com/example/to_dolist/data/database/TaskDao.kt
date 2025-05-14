package com.example.to_dolist.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.to_dolist.data.model.TaskEntity

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("SELECT * FROM tasks ORDER BY timestamp DESC")
    fun getAllTasks(): LiveData<List<TaskEntity>>
}