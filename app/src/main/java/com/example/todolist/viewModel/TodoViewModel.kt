package com.example.todolist.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todolist.dataClass.Todo
import com.example.todolist.dataBase.TodoDatabase
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val todoDao = TodoDatabase.getDatabase(application).todoDao()
    val allTodos: LiveData<List<Todo>> = todoDao.getAllTodos()

    fun addTodo(todo: Todo) {
        viewModelScope.launch {
            todoDao.insert(todo)
        }
    }
}

