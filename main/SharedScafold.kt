package com.example.nokianew

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width

@Composable
fun SharedScaffold(navController: NavHostController, content: @Composable (PaddingValues) -> Unit) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("NOKIA") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                backgroundColor = Color(0xFF007BFF),  // Change the background color here
                contentColor = Color.White            // Change the content (icon/text) color here
            )
        },
        drawerContent = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White // Change the drawer background color here
            ) {
                DrawerContent(navController)
            }
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}


@Composable
fun DrawerContent(navController: NavHostController) {
    Column(modifier = Modifier.width(200.dp)) {
        Text(
            text = "Menu",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(16.dp)

        )
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        // Dashboard Section
        TextButton(onClick = { navController.navigate("dashboard") },
            modifier = Modifier.padding(start = 8.dp) ) {
            Text("Dashboard",
                color = Color(0xFF007BFF),
                fontSize = 20.sp)
        }

        // Execution Section
        TextButton(onClick = { navController.navigate("execution") },
            modifier = Modifier.padding(start = 8.dp) ) {
            Text("Execution",
                color = Color(0xFF007BFF),
                fontSize = 20.sp)

        }
    }
}