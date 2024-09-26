package com.example.nokianew


import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.view.MenuItem
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

class ExecutionScreen : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_execute) // Ensure this uses your updated layout with DrawerLayout

        // Initialize the drawer and toolbar
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)

        // Set up the toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu) // Custom menu icon

        // Set up the ActionBarDrawerToggle
        drawerToggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.drawer_open, R.string.drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Set up navigation item click listener
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_dashboard -> {
                    Toast.makeText(this, "Dashboard clicked", Toast.LENGTH_SHORT).show()
                    // Navigate to Dashboard or handle the click here
                }
                R.id.nav_execution -> {
                    Toast.makeText(this, "Execution clicked", Toast.LENGTH_SHORT).show()
                    // You are already on this screen, but you could refresh or perform another action
                }
            }
            drawerLayout.closeDrawers() // Close drawer after item is clicked
            true
        }

        // Your existing execution logic (spinners, buttons, etc.) continues below
        setupExecutionUI() // Existing setup logic
    }

    private fun setupExecutionUI() {
        // Your existing code for spinners, buttons, execution logic, etc.
        val spinner: Spinner = findViewById(R.id.spinner_test_environment)
        // rest of the logic...
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
