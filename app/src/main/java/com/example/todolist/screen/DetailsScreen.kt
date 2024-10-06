package com.example.todolist.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.todolist.dataClass.Todo
import com.example.todolist.greenColor
import com.example.todolist.viewModel.TodoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsScreen(navController: NavController, viewModel: TodoViewModel) {
    var todoText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add TODO Details", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(greenColor),
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            TextField(
                value = todoText,
                onValueChange = { todoText = it },
                label = { Text("TODO") },
                singleLine = true,
                textStyle = TextStyle( fontSize = 20.sp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if ( todoText.isEmpty()) {
                        // Set error state and navigate back
                        navController.previousBackStackEntry?.savedStateHandle?.set("error", true)
                        navController.popBackStack() // Navigate back to the previous screen
                    } else {
                        isLoading = true
                        scope.launch {
                            // Simulate a network/database operation
                            delay(3000) // Simulate 3 seconds delay
                            viewModel.addTodo(Todo(text = todoText))
                            isLoading = false
                            navController.popBackStack() // Navigate back to the previous screen
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(greenColor),
            ) {
                Text("Add TODO")
            }
        }
        if (isLoading) {
            loadingDialog()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview() {
    DetailsScreen( navController = rememberNavController(), viewModel())
}

