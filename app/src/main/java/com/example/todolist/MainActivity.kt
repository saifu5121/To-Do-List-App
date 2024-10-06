package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolist.screen.DetailsScreen
import com.example.todolist.screen.HomeScreen
import com.example.todolist.viewModel.TodoViewModel

val greenColor = Color(0xFF388642)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val viewModel: TodoViewModel = viewModel()

            NavHost(navController = navController, startDestination = "main") {
                composable("main") { HomeScreen(navController, viewModel) }
                composable("details") { DetailsScreen(navController, viewModel) }
            }
        }
    }
}



