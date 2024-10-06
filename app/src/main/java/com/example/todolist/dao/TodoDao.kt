package com.example.todolist.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.todolist.dataClass.Todo

@Dao
interface TodoDao {
    @Insert
    suspend fun insert(todo: Todo)

    @Query("SELECT * FROM todo_table")
    fun getAllTodos(): LiveData<List<Todo>>
}
