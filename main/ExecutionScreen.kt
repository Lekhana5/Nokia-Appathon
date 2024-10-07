package com.example.nokianew

import Job
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* // Ensure this is imported for padding, spacing, and arrangement.
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import getCurrentTime
import kotlinx.coroutines.launch

val BlueColor = Color(0xFF007BFF)
val WhiteColor = Color.White

@Composable
fun ExecutionScreen(navController: NavHostController,jobExecutionViewModel: JobExecutionViewModel = viewModel()) {
    SharedScaffold(navController = navController) {
        val context = LocalContext.current

        // State variables
        var selectedJob by remember { mutableStateOf("Please select a job") }
        var selectedTestProfile by remember { mutableStateOf("Select an option") }
        var selectedUEProfile by remember { mutableStateOf("Select an option") }

        var testSuitesList = listOf("4G-VOLTE", "4g_end_to_end_testing")
        var testCasesList = listOf("4G_CMM_UE_attach_with_GUTI", "4G_CMM_Network_initiated_data_session", "4G_CMM_MT_SMS_Connected_Mode")
        var appServerList = listOf("APP_01", "APP_02")
        var useCasesList = listOf("Functional", "NonFunctional")

        val selectedTestSuites = remember { mutableStateListOf<String>() }
        val selectedTestCases = remember { mutableStateListOf<String>() }
        val selectedAppServer = remember { mutableStateListOf<String>() }
        val selectedUseCases = remember { mutableStateListOf<String>() }

        // Scroll state for managing scrolling
        val scrollState = rememberScrollState()


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState) // Add vertical scrolling
        ) {
            Text(
                "Select the test case you want to execute\n",
                fontSize = 18.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            // Job Selection Dropdown
            DropdownMenuComponent(
                label = "Test Environment",
                selectedOption = selectedJob,
                options = listOf("Please select a job", "DEMOJOB", "TESTJOB"),
                onOptionSelected = { selectedJob = it }
            )

            // Test Profile Dropdown
            DropdownMenuComponent(
                label = "Test Profile",
                selectedOption = selectedTestProfile,
                options = listOf("Select an option", "chennai_lab_offline.py", "chennai_lab_online.py"),
                onOptionSelected = { selectedTestProfile = it }
            )

            // UE Profile Dropdown
            DropdownMenuComponent(
                label = "UE Profile",
                selectedOption = selectedUEProfile,
                options = listOf("Select an option", "chennai_ue_01.yml", "miniauto.yml"),
                onOptionSelected = { selectedUEProfile = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Multi-Select Buttons
            MultiSelectComponent(label = "Test Suites", options = testSuitesList, selectedItems = selectedTestSuites)
            MultiSelectComponent(label = "Test Cases", options = testCasesList, selectedItems = selectedTestCases)
            MultiSelectComponent(label = "App Server", options = appServerList, selectedItems = selectedAppServer)
            MultiSelectComponent(label = "Use Cases", options = useCasesList, selectedItems = selectedUseCases)

            Spacer(modifier = Modifier.height(16.dp))

            // Execute Button
            Button(
                onClick = {
                    if (validateSelections(selectedJob, selectedTestProfile, selectedUEProfile, selectedTestSuites, selectedTestCases, selectedAppServer, selectedUseCases, context)) {
                        val newJob = Job(
                            sessionId = "new_job_${System.currentTimeMillis()}",
                            testrunName = selectedJob,
                            execDate = getCurrentTime(),
                            username = "ntspuser", // Example user
                            total = selectedTestCases.size,
                            executed = selectedTestCases.size,
                            pass = selectedTestCases.count { /* logic to determine pass/fail */ true },
                            fail = selectedTestCases.size - selectedTestCases.count { /* logic */ true },
                            status = "success"
                        )
                        jobExecutionViewModel.addJob(newJob)

                        // Proceed with execution logic
                        Toast.makeText(context, "Execution started with selected parameters.", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50))

            ) {
                Text(text = "Execute",
                    color = WhiteColor,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(16.dp))


            }
        }
    }


@Composable
fun DropdownMenuComponent(label: String, selectedOption: String, options: List<String>, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Medium
            )
        )
        Button(
            onClick = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = WhiteColor),
            border = BorderStroke(1.dp, BlueColor),
            shape = RectangleShape
        ) {
            Text(
                text = selectedOption,
                color = BlueColor,
                fontSize = 12.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = BlueColor)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onOptionSelected(option)
                    expanded = false
                }) {
                    Text(
                        text = option,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun MultiSelectComponent(label: String, options: List<String>, selectedItems: MutableList<String>) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Medium
            )
        )
        Button(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = WhiteColor),
            border = BorderStroke(1.dp, BlueColor),
            shape = RectangleShape
        ) {
            Text(
                text = if (selectedItems.isNotEmpty()) selectedItems.joinToString(", ") else "Select $label",
                color = BlueColor,
                fontSize = 12.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal
            )
        }

        if (expanded) {
            AlertDialog(
                onDismissRequest = { expanded = false },
                title = {
                    Text(
                        text = "Select $label",
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Medium
                    )
                },
                buttons = {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        options.forEach { option ->
                            val isSelected = selectedItems.contains(option)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = {
                                        if (isSelected) selectedItems.remove(option) else selectedItems.add(option)
                                    },
                                    colors = CheckboxDefaults.colors(checkedColor = BlueColor)
                                )
                                Text(
                                    text = option,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Default,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { expanded = false },
                            colors = ButtonDefaults.buttonColors(backgroundColor = WhiteColor),
                            border = BorderStroke(1.dp, BlueColor),
                            shape = RectangleShape
                        ) {
                            Text(
                                text = "OK",
                                color = BlueColor,
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            )
        }
    }
}

// Function to validate selections
private fun validateSelections(
    selectedJob: String,
    selectedTestProfile: String,
    selectedUEProfile: String,
    selectedTestSuites: List<String>,
    selectedTestCases: List<String>,
    selectedAppServer: List<String>,
    selectedUseCases: List<String>,
    context: android.content.Context
): Boolean {
    if (selectedJob == "Please select a job") {
        Toast.makeText(context, "Please select a valid job", Toast.LENGTH_SHORT).show()
        return false
    }
    if (selectedTestProfile == "Select an option") {
        Toast.makeText(context, "Please select a valid Test Profile", Toast.LENGTH_SHORT).show()
        return false
    }
    if (selectedUEProfile == "Select an option") {
        Toast.makeText(context, "Please select a valid UE Profile", Toast.LENGTH_SHORT).show()
        return false
    }
    if (selectedTestSuites.isEmpty()) {
        Toast.makeText(context, "Please select at least one Test Suite", Toast.LENGTH_SHORT).show()
        return false
    }
    if (selectedTestCases.isEmpty()) {
        Toast.makeText(context, "Please select at least one Test Case", Toast.LENGTH_SHORT).show()
        return false
    }
    if (selectedAppServer.isEmpty()) {
        Toast.makeText(context, "Please select at least one App Server", Toast.LENGTH_SHORT).show()
        return false
    }
    if (selectedUseCases.isEmpty()) {
        Toast.makeText(context, "Please select at least one Use Case", Toast.LENGTH_SHORT).show()
        return false
    }
    return true
}