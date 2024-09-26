package com.example.nokia

import android.graphics.Color
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var selectedTestSuites: BooleanArray
    private lateinit var selectedTestCases: BooleanArray
    private lateinit var selectedAppServer: BooleanArray
    private lateinit var selectedUseCases: BooleanArray

    private val selectedTestSuitesNames = mutableListOf<String>()
    private val selectedTestCasesNames = mutableListOf<String>()
    private val selectedAppServerNames = mutableListOf<String>()
    private val selectedUseCasesNames = mutableListOf<String>()

    private var testSuitesList = emptyArray<String>()
    private var testCasesList = emptyArray<String>()
    private var appServerList = emptyArray<String>()
    private var useCasesList = emptyArray<String>()
    private var testProfileList = emptyArray<String>()
    private var ueProfileList = emptyArray<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner: Spinner = findViewById(R.id.spinner_test_environment)
        val extraSpinnersLayout: GridLayout = findViewById(R.id.grid_layout_extra_spinners)
        val spinnerTestProfile = findViewById<Spinner>(R.id.spinner_test_profile)
        val spinnerUEProfile = findViewById<Spinner>(R.id.spinner_ue_profile)
        val buttonAppServer = findViewById<Button>(R.id.spinner_app_server)
        val buttonTestSuites = findViewById<Button>(R.id.spinner_test_suites)
        val buttonTestCases = findViewById<Button>(R.id.spinner_test_cases)
        val buttonUseCases = findViewById<Button>(R.id.spinner_use_case)
        val executeButton: Button = findViewById(R.id.execute_button)

        // Populate jobs for Test Environment spinner with placeholder
        populateJobsWithPlaceholder(spinner)

        // Test environment spinner selection listener
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    // Placeholder is selected, no action needed
                    (view as? TextView)?.setTextColor(Color.GRAY)
                    extraSpinnersLayout.visibility = View.GONE
                } else {
                    // Proceed with normal selection
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    extraSpinnersLayout.visibility = View.VISIBLE
                    // Trigger the pseudo API call and populate the dropdowns
                    fetchAndPopulateDropDowns(selectedItem)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No-op
            }
        }

        // Initialize multi-select variables
        initializeMultiSelectOptions()

        // Set up the multi-select spinners (buttons that trigger dialogs)
        setupMultiSelectDialogs()

        executeButton.setOnClickListener {
            // Get the selected job from the spinner
            val selectedJob = spinner.selectedItem.toString()

            // Check if the user has selected the default placeholder ("Please select a job")
            if (selectedJob == "Please select a job") {
                // Show an error message to the user
                Toast.makeText(this, "Please select a valid job before execution", Toast.LENGTH_SHORT).show()
            } else {
                // If a valid job is selected, proceed with the job execution
                sendAndParseJobExecutionRequest(spinner, spinnerTestProfile, spinnerUEProfile)
            }
        }

    }

    private fun fetchAndPopulateDropDowns(jobId: String) {
        try {
            val jsonResponse = sendSecondPseudoApiCall(jobId)
            val output = jsonResponse.getJSONObject("output")

            // Populate lists based on the JSON output
            useCasesList = jsonArrayToStringArray(output.getJSONArray("Use Case"))
            testSuitesList = jsonArrayToStringArray(output.getJSONArray("test suites"))
            testCasesList = jsonArrayToStringArray(output.getJSONArray("test cases"))
            appServerList = jsonArrayToStringArray(output.getJSONArray("app server"))

            // Initialize and populate single-selection spinners
            initializeMultiSelectOptions()
            populateSingleSelectSpinner(R.id.spinner_ue_profile, output.getJSONArray("ue profile"))
            populateSingleSelectSpinner(R.id.spinner_test_profile, output.getJSONArray("test profiles"))

        } catch (e: JSONException) {
            Toast.makeText(this, "Error parsing JSON data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun populateJobsWithPlaceholder(spinner: Spinner) {
        try {
            val jsonResponse = sendPseudoApiCall()
            val jobList = mutableListOf<String>()

            // Add the placeholder as the first item (this won't be selectable)
            jobList.add("Please select a job")

            val outputArray = jsonResponse.getJSONArray("output")
            // Add the jobs from the API response to the list
            for (i in 0 until outputArray.length()) {
                val job = outputArray.getJSONObject(i)
                val jobName = job.getString("job_name")
                jobList.add(jobName)
            }

            // Set up the adapter with the job list
            val adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jobList) {
                override fun isEnabled(position: Int): Boolean {
                    // Disable the first item (placeholder) from being selected
                    return position != 0
                }

                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val tv = view as TextView
                    // Set the text color of the placeholder to gray
                    if (position == 0) {
                        tv.setTextColor(Color.BLACK)
                    } else {
                        tv.setTextColor(Color.BLACK)
                    }
                    return view
                }
            }

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            // Set default selection to the placeholder
            spinner.setSelection(0)
        } catch (e: JSONException) {
            Toast.makeText(this, "Error parsing JSON data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun populateSingleSelectSpinner(spinnerId: Int, jsonArray: JSONArray) {
        val spinner: Spinner = findViewById(spinnerId)
        val list = jsonArrayToStringArray(jsonArray).toMutableList()
        list.add(0, "Select an option")  // Add a default option
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun initializeMultiSelectOptions() {
        selectedTestSuites = BooleanArray(testSuitesList.size) { false }
        selectedTestCases = BooleanArray(testCasesList.size) { false }
        selectedAppServer = BooleanArray(appServerList.size) { false }
        selectedUseCases = BooleanArray(useCasesList.size) { false }
    }

    private fun setupMultiSelectDialogs() {
        // Handle "Test Suites" multi-select
        findViewById<Button>(R.id.spinner_test_suites).setOnClickListener {
            val button = it as Button
            showMultiSelectDialog(
                "Select Test Suites",
                testSuitesList,
                selectedTestSuites,
                selectedTestSuitesNames,
                button
            )

        }

        // Handle "Test Cases" multi-select
        findViewById<Button>(R.id.spinner_test_cases).setOnClickListener {
            val button = it as Button
            showMultiSelectDialog(
                "Select Test Cases",
                testCasesList,
                selectedTestCases,
                selectedTestCasesNames,
                button
            )
        }

        // Handle "App Server" multi-select
        findViewById<Button>(R.id.spinner_app_server).setOnClickListener {
            val button = it as Button
            showMultiSelectDialog(
                "Select App Server",
                appServerList,
                selectedAppServer,
                selectedAppServerNames,
                button
            )
        }

        // Handle "Use Cases" multi-select
        findViewById<Button>(R.id.spinner_use_case).setOnClickListener {
            val button = it as Button
            showMultiSelectDialog(
                "Select Use Cases",
                useCasesList,
                selectedUseCases,
                selectedUseCasesNames,
                button
            )
        }
    }

    private fun showMultiSelectDialog(
        title: String,
        items: Array<String>,
        selectedItems: BooleanArray,
        selectedNames: MutableList<String>,
        button: Button
    ) {
        if (items.isEmpty()) {
            Toast.makeText(this, "Select test environment", Toast.LENGTH_SHORT).show()
            return
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMultiChoiceItems(items, selectedItems) { _, which, isChecked ->
            selectedItems[which] = isChecked
            if (isChecked) {
                selectedNames.add(items[which])
            } else {
                selectedNames.remove(items[which])
            }
        }
        builder.setPositiveButton("OK") { _, _ ->
            Toast.makeText(this, "Selected: ${selectedNames.joinToString(", ")}", Toast.LENGTH_SHORT).show()
            for (i in selectedItems.indices) {
                selectedItems[i] = selectedItems[i]
            }

            // Update the button text with the selected items or "Select Test Suites" if none
            if (selectedNames.isNotEmpty()) {
                button.text = selectedNames.joinToString(", ")
            } else {
                button.text = "Select Test Suites" // Default text if nothing is selected
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun sendSecondPseudoApiCall(jobId: String): JSONObject {
        val requestJson = JSONObject()
        requestJson.put("ntsp_username", "ntspuser")
        requestJson.put("ntsp_type", "auto")
        requestJson.put("ntsp_action", "fetch_test_suites_and_profiles")

        val input = JSONObject()
        val taInput = JSONObject()
        taInput.put("job_id", jobId)
        taInput.put("testcase_name", "")
        taInput.put("test_services", JSONArray())
        taInput.put("test_suites", JSONArray())
        input.put("ta_input", taInput)

        requestJson.put("ntsp_input", input)

        // For simplicity, assume we're reading from a mock2.json file in assets
        val jsonResponseString = readJsonFromAssets("mock2.json")
        return JSONObject(jsonResponseString)
    }

    private fun sendPseudoApiCall(): JSONObject {
        val jsonResponseString = readJsonFromAssets("mock_data.json")
        return JSONObject(jsonResponseString)
    }

    private fun readJsonFromAssets(fileName: String): String {
        var jsonString: String? = null
        try {
            jsonString = assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
        return jsonString ?: "{}"
    }

    private fun jsonArrayToStringArray(jsonArray: JSONArray): Array<String> {
        val array = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            array.add(jsonArray.getString(i))
        }
        return array.toTypedArray()
    }
    private fun sendAndParseJobExecutionRequest(
        spinner: Spinner,
        spinnerTestProfile: Spinner,
        spinnerUEProfile: Spinner
    ) {
        val selectedItemPosition = spinnerTestProfile.selectedItemPosition

        if (selectedItemPosition > 0) {
        val testEnvironment = spinner.selectedItem.toString()
        val testProfile = spinnerTestProfile.selectedItem.toString()
        val ueProfile = spinnerUEProfile.selectedItem.toString()

        // Get values from multi-select buttons
        val appServer = selectedAppServerNames.joinToString(", ")
        val testSuites = selectedTestSuitesNames.joinToString(", ")
        val testCases = selectedTestCasesNames.joinToString(", ")
        val useCases = selectedUseCasesNames.joinToString(", ")

        // Prepare and send the request with these values
        val testRunName = testEnvironment
        val jobId = testEnvironment
        val serviceSelected = useCases.split(", ")
        val suiteSelected = testSuites.split(", ")
        val appServerList = appServer.split(", ")

        // Call the function to send the request and get the response
        val response = sendJobExecutionRequest(
            testRunName,
            jobId,
            testCases.split(", "),
            serviceSelected,
            suiteSelected,
            testProfile,
            ueProfile,
            "",
            "",
            1,
            1,
            "",
            appServerList
        )

        // Parse the response
        val sessionId = response.getJSONObject("output").getString("ntsp_session_id")
        val message = response.getString("message")
        val status = response.getString("status")

        // Display the result using a dialog
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Execution Result")
        dialogBuilder.setMessage("Status: $status\n\nMessage: $message\n\nSession ID: $sessionId\n\nGo back to the dashboard to view execution status")

        dialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss() // Close the dialog when the button is clicked
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }}

    private fun sendJobExecutionRequest(
        testRunName: String,
        jobId: String,
        testcases: List<String>,
        serviceSelected: List<String>,
        suiteSelected: List<String>,
        envProfile: String,
        ueProfile: String,
        inTag: String,
        exTag: String,
        rerun: Int,
        iteration: Int,
        validationTemplate: String,
        appServer: List<String>
    ): JSONObject {
        // Create the request JSON based on the selected options
        val requestJson = JSONObject()

        requestJson.put("ntsp_type", "auto")
        requestJson.put("ntsp_username", "ntspuser")
        requestJson.put("ntsp_action", "job_execution")

        // Creating the ntsp_input -> ntsp_data structure
        val ntspInput = JSONObject()
        val ntspData = JSONObject()

        ntspData.put("test_run_name", testRunName)
        ntspData.put("job_id", jobId)
        ntspData.put("executor_type", "auto")
        ntspData.put("sub_executor_type", "ntav")
        ntspData.put("testcases", JSONArray(testcases))
        ntspData.put("service_selected", JSONArray(serviceSelected))
        ntspData.put("suite_selected", JSONArray(suiteSelected))
        ntspData.put("envprofile", envProfile)
        ntspData.put("ueprofile", ueProfile)
        ntspData.put("in_tag", inTag)
        ntspData.put("ex_tag", exTag)
        ntspData.put("rerun", rerun)
        ntspData.put("iteration", iteration)
        ntspData.put("multi_env_tc", "")
        ntspData.put("validation_template", validationTemplate)
        ntspData.put("app_server", JSONArray(appServer))

        ntspInput.put("ntsp_data", ntspData)
        requestJson.put("ntsp_input", ntspInput)

        // Show the AlertDialog with the request JSON as a string
        showRequestDialog(requestJson.toString())

        // Now read the mock3.json file and simulate the API response
        return readJobExecutionResponse("mock3.json")
    }

    // Helper function to show AlertDialog
    private fun showRequestDialog(requestString: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Request Details")
        builder.setMessage(requestString)

        // Add a button to close the dialog
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }



    private fun readJobExecutionResponse(fileName: String): JSONObject {
        var jsonString: String? = null
        try {
            jsonString = assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

        return JSONObject(jsonString ?: "{}")
    }


}
