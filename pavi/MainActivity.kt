package com.example.nokianew

import DashboardScreen
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "login") {
                composable("login") {
                    LoginScreen(navController = navController)
                }
                composable("dashboard") {
                    DashboardScreen(navController = navController)
                }
                composable("execution") {
                    StartExecutionActivity()
                }
            }
        }
    }
}
@Composable
fun StartExecutionActivity() {
    val context = LocalContext.current

    // Immediately start the ExecutionScreen activity using Intent
    val intent = Intent(context, ExecutionScreen::class.java)
    context.startActivity(intent)
}