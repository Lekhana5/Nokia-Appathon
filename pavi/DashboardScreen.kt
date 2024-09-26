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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.nokianew.SharedScaffold
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

// Hardcoded function simulating the API response
fun getHardcodedJobs(): List<Job> {
    return listOf(
        Job(
            sessionId = "f1fe425_a5a_1725813420046",
            testrunName = "TESTJOB",
            execDate = 1725813420046,
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
            execDate = 1725813230831,
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
            execDate = 1725813048650,
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
            execDate = 1725812871949,
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
fun DashboardScreen(navController: NavHostController) {
    SharedScaffold(navController = navController) {
        val jobs = getHardcodedJobs()

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
            ) {
                Column {
                    // Table Headers
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Job Name", modifier = Modifier.width(100.dp).align(alignment = Alignment.CenterVertically))
                        Text("Executed Date", modifier = Modifier.width(150.dp).align(alignment = Alignment.CenterVertically))
                        Text("Username", modifier = Modifier.width(80.dp).align(alignment = Alignment.CenterVertically))
                        Text("Total", modifier = Modifier.width(50.dp).align(alignment = Alignment.CenterVertically))
                        Text("Executed", modifier = Modifier.width(80.dp).align(alignment = Alignment.CenterVertically))
                        Text("Pass", modifier = Modifier.width(50.dp).align(alignment = Alignment.CenterVertically))
                        Text("Fail", modifier = Modifier.width(50.dp).align(alignment = Alignment.CenterVertically))
                        Text("Status", modifier = Modifier.width(50.dp).align(alignment = Alignment.CenterVertically))
                        Text("Console Log", modifier = Modifier.width(80.dp).align(alignment = Alignment.CenterVertically))
                    }

                    Divider()

                    // Data Rows
                    jobs.forEach { job ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp), // Add padding to avoid content congestion
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(job.testrunName, modifier = Modifier.width(100.dp).align(alignment = Alignment.CenterVertically))
                            Text(formatDate(job.execDate), modifier = Modifier.width(150.dp).align(alignment = Alignment.CenterVertically))
                            Text(job.username, modifier = Modifier.width(80.dp).align(alignment = Alignment.CenterVertically))
                            Text(job.total.toString(), modifier = Modifier.width(50.dp).align(alignment = Alignment.CenterVertically))
                            Text(job.executed.toString(), modifier = Modifier.width(80.dp).align(alignment = Alignment.CenterVertically))
                            Text(job.pass.toString(), modifier = Modifier.width(50.dp).align(alignment = Alignment.CenterVertically))
                            Text(job.fail.toString(), modifier = Modifier.width(50.dp).align(alignment = Alignment.CenterVertically))

                            // Status Icon
                            StatusIcon(job.status, modifier = Modifier.width(50.dp).align(alignment = Alignment.CenterVertically))

                            // Console Log Icon
                            Icon(Icons.Default.List, contentDescription = "Console Log", modifier = Modifier.width(50.dp).align(alignment = Alignment.CenterVertically))
                        }
                        Divider()
                    }
                }
            }
        }
    }
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
