package com.example.nokianew

import DashboardScreen
import Job
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val jobExecutionViewModel: JobExecutionViewModel = viewModel()

            NavHost(navController = navController, startDestination = "splash_screen") {
                composable("splash_screen") {
                    SplashScreen(navController = navController)
                }
                composable("login") {
                    LoginScreen(navController = navController)
                }
                composable("dashboard") {
                    DashboardScreen(navController = navController,jobExecutionViewModel)
                }
                composable("execution") {
                    ExecutionScreen(navController = navController,jobExecutionViewModel)
                }
            }
        }
    }
}

class JobExecutionViewModel : ViewModel() {

    // LiveData for the list of executed jobs
    private val _jobs = MutableLiveData<List<Job>>(emptyList())
    val jobs: LiveData<List<Job>> = _jobs
    private fun clearJobs() {
        _jobs.value = emptyList()
    }

    // Function to add a new job to the list
    fun addJob(job: Job) {
        // Add the new job to the existing list
        _jobs.value = _jobs.value?.plus(job)
    }
}
