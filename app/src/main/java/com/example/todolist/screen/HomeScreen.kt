package com.example.todolist.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.todolist.greenColor
import com.example.todolist.viewModel.TodoViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: TodoViewModel) {
    val todos by viewModel.allTodos.observeAsState(emptyList())
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val snackbarHostState = remember { SnackbarHostState() }


    // Check for error condition
    val errorState = navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("error")?.observeAsState()

    // Show Snackbar if there's an error
    LaunchedEffect(errorState?.value) {
        if (errorState?.value == true) {
            snackbarHostState.showSnackbar("Failed to add TODO!!")
            // Clear the error state after showing Snackbar
            navController.currentBackStackEntry?.savedStateHandle?.set("error", false)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TODO List", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(greenColor),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("details") },
                containerColor = greenColor,
                contentColor = Color.White,
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add TODO")
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    CustomSnackbar(snackbarData = data,)
                }
            )
        }


    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                placeholder = { Text("Search TODOs") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Icon"
                    )
                },

            )
            if (todos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Press the + button to add a TODO item",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                val filteredList = todos.filter { it.text.contains(searchQuery.text, ignoreCase = true) }
                LazyColumn {
                    items(filteredList) { todo ->
                        TodoItem(todo.text)
                    }
                }
            }
        }
    }
}

@Composable
fun TodoItem(todo: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors  = CardDefaults.cardColors(greenColor),
    ) {
        Text(
            text = todo,
            modifier = Modifier.padding(16.dp),
            color = Color.White
        )
    }
}

@Composable
fun loadingDialog(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.5f)) // Background dimming
            .wrapContentSize(Alignment.Center), // Center the box
        contentAlignment = Alignment.TopStart

    ) {
        Card(
            modifier = Modifier
                .size(150.dp) // Card size 300dp x 300dp
                .padding(16.dp), // Optional padding inside the card
            colors =  CardDefaults.cardColors(Color.White) // Card color

        ) {
            Box(
                modifier = Modifier
                    .size(300.dp) // Box size 300dp x 300dp
                    .padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                CircularProgressIndicator(
                    color = greenColor, // Loading color
                    strokeWidth = 5.dp, // Adjust the thickness of the loader
                    modifier = Modifier.size(100.dp) // Loading size 50dp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen( navController = rememberNavController(), viewModel())
}

@Composable
fun CustomSnackbar(snackbarData: SnackbarData, ) {

    Card(
        colors =  CardDefaults.cardColors(Color.White),
        elevation =  CardDefaults.cardElevation(5.dp),
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .background(Color.Transparent, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(
                text = snackbarData.visuals.message,
                color = Color.Red,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
