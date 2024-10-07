import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.nokianew.SharedScaffold
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nokianew.JobExecutionViewModel

data class Job(
    val sessionId: String,
    val testrunName: String,
    val execDate: Long,
    val username: String,
    val total: Int,
    val executed: Int,
    val pass: Int,
    val fail: Int,
    val status: String,
    val consoleLog: String = "Console Log"
)
fun getCurrentTime(): Long {
    return System.currentTimeMillis()
}

// Hardcoded function simulating the API response
fun getHardcodedJobs(): List<Job> {
    val currentTime = getCurrentTime() // Get current system time
    return listOf(
        Job(
            sessionId = "f1fe425_a5a_1725813420046",
            testrunName = "TESTJOB",
            execDate = currentTime,
            username = "ntspuser",
            total = 1,
            executed = 1,
            pass = 0,
            fail = 1,
            status = "failed"
        ),
        Job(
            sessionId = "96d19524c_8_1725813230831",
            testrunName = "TESTJOB",
            execDate = currentTime,
            username = "ntspuser",
            total = 1,
            executed = 1,
            pass = 0,
            fail = 1,
            status = "failed"
        ),
        Job(
            sessionId = "d36e3a230334_1725813048650",
            testrunName = "TESTJOB",
            execDate = currentTime,
            username = "ntspuser",
            total = 1,
            executed = 1,
            pass = 1,
            fail = 0,
            status = "success"
        ),
        Job(
            sessionId = "0c64c5b1f23d_1725812871949",
            testrunName = "TESTJOB",
            execDate = currentTime,
            username = "ntspuser",
            total = 1,
            executed = 0,
            pass = 0,
            fail = 0,
            status = "inprogress"
        )
    )
}


@Composable
fun DashboardScreen(navController: NavHostController, jobExecutionViewModel: JobExecutionViewModel = viewModel()) {
    SharedScaffold(navController = navController) {
        val hardcodedjobs = getHardcodedJobs()

        val dynamicJobs by jobExecutionViewModel.jobs.observeAsState(emptyList())
        Log.d("DashboardScreen", "Dynamic jobs count: ${dynamicJobs.size}")

        val allJobs = hardcodedjobs + dynamicJobs
        // Vertical scroll state for the entire column
        val verticalScrollState = rememberScrollState()

        // Horizontal scroll state for the table
        val horizontalScrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
                .verticalScroll(verticalScrollState) // Enable vertical scrolling
        ) {
            // Wrapping the entire table in horizontal scroll
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(horizontalScrollState) // Enable horizontal scrolling
                    .padding(4.dp)
            ) {
                Column {
                    // Table Headers with horizontal divider
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(Color(0xFF4CAF50)), // Background color for header
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Job Name",
                            color=Color.White,
                            modifier = Modifier
                                .width(100.dp)
                                .align(alignment = Alignment.CenterVertically)
                                .padding(4.dp)

                        )
                        VerticalDivider()
                        Text(
                            "Executed Date",
                            color=Color.White,
                            modifier = Modifier
                                .width(150.dp)
                                .align(alignment = Alignment.CenterVertically)
                                .padding(4.dp)
                        )
                        VerticalDivider()
                        Text(
                            "Username",
                            color=Color.White,
                            modifier = Modifier
                                .width(80.dp)
                                .align(alignment = Alignment.CenterVertically)
                                .padding(4.dp)
                        )
                        VerticalDivider()
                        Text(
                            "Total",
                            color=Color.White,
                            modifier = Modifier
                                .width(50.dp)
                                .align(alignment = Alignment.CenterVertically)
                                .padding(4.dp)
                        )
                        VerticalDivider()
                        Text(
                            "Executed",
                            color=Color.White,
                            modifier = Modifier
                                .width(80.dp)
                                .align(alignment = Alignment.CenterVertically)
                                .padding(4.dp)
                        )
                        VerticalDivider()
                        Text(
                            "Pass",
                            color=Color.White,
                            modifier = Modifier
                                .width(50.dp)
                                .align(alignment = Alignment.CenterVertically)
                                .padding(4.dp)
                        )
                        VerticalDivider()
                        Text(
                            "Fail",
                            color=Color.White,
                            modifier = Modifier
                                .width(50.dp)
                                .align(alignment = Alignment.CenterVertically)
                                .padding(4.dp)
                        )
                        VerticalDivider()
                        Text(
                            "Status",
                            color=Color.White,
                            modifier = Modifier
                                .width(50.dp)
                                .align(alignment = Alignment.CenterVertically)
                                .padding(4.dp)
                        )
                        VerticalDivider()
                        Text(
                            "Console Log",
                            color=Color.White,
                            modifier = Modifier
                                .width(80.dp)
                                .align(alignment = Alignment.CenterVertically)
                                .padding(4.dp)
                        )
                    }

                    Divider(thickness = 2.dp, color = Color.Black) // Horizontal divider after header

                    // Data Rows
                    allJobs.forEach { job ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                job.testrunName,
                                modifier = Modifier
                                    .width(100.dp)
                                    .align(alignment = Alignment.CenterVertically)
                                    .padding(4.dp)
                            )
                            VerticalDivider()
                            Text(
                                formatDate(job.execDate),
                                modifier = Modifier
                                    .width(150.dp)
                                    .align(alignment = Alignment.CenterVertically)
                                    .padding(4.dp)
                            )
                            VerticalDivider()
                            Text(
                                job.username,
                                modifier = Modifier
                                    .width(80.dp)
                                    .align(alignment = Alignment.CenterVertically)
                                    .padding(4.dp)
                            )
                            VerticalDivider()
                            Text(
                                job.total.toString(),
                                modifier = Modifier
                                    .width(50.dp)
                                    .align(alignment = Alignment.CenterVertically)
                                    .padding(4.dp)
                            )
                            VerticalDivider()
                            Text(
                                job.executed.toString(),
                                modifier = Modifier
                                    .width(80.dp)
                                    .align(alignment = Alignment.CenterVertically)
                                    .padding(4.dp)
                            )
                            VerticalDivider()
                            Text(
                                job.pass.toString(),
                                modifier = Modifier
                                    .width(50.dp)
                                    .align(alignment = Alignment.CenterVertically)
                                    .padding(4.dp)
                            )
                            VerticalDivider()
                            Text(
                                job.fail.toString(),
                                modifier = Modifier
                                    .width(50.dp)
                                    .align(alignment = Alignment.CenterVertically)
                                    .padding(4.dp)
                            )
                            VerticalDivider()
                            StatusIcon(
                                job.status,
                                modifier = Modifier
                                    .width(50.dp)
                                    .align(alignment = Alignment.CenterVertically)
                                    .padding(4.dp)
                            )
                            VerticalDivider()
                            Icon(
                                Icons.Default.List,
                                contentDescription = "Console Log",
                                modifier = Modifier
                                    .width(80.dp)
                                    .align(alignment = Alignment.CenterVertically)
                                    .padding(4.dp)
                            )
                        }
                        Divider(thickness = 2.dp) // Horizontal divider after each row
                    }
                }
            }
        }
    }
}

@Composable
fun VerticalDivider() {
    Spacer(
        modifier = Modifier
            .width(4.dp)
            .fillMaxHeight()
            .background(Color.Black) // Divider color
    )
}

@Composable
fun StatusIcon(status: String, modifier: Modifier = Modifier) {
    when (status) {
        "inprogress" -> {
            Icon(Icons.Default.Downloading, contentDescription = "In Progress", tint = Color.Blue, modifier = modifier)
        }
        "success" -> {
            Icon(Icons.Default.CheckCircle, contentDescription = "Success", tint = Color.Green, modifier = modifier)
        }
        "failed" -> {
            Icon(Icons.Default.Cancel, contentDescription = "Failed", tint = Color.Red, modifier = modifier)
        }
        else -> {
            Icon(Icons.Default.ErrorOutline, contentDescription = "Unknown", tint = Color.Gray, modifier = modifier)
        }
    }
}

fun formatDate(epochMillis: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(epochMillis))
}
