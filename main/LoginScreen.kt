package com.example.nokianew

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val validEmail = remember { "user@gmail.com" }
    val validPassword = remember { "password123" }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Background image
            Image(
                painter = painterResource(id = R.drawable.bg1), // Replace with your background image resource
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // Adjust based on how you want the image to be displayed
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo3), // Replace with your logo resource
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(300.dp) // Adjust the size as needed
                        .padding(bottom = 10.dp), // Spacing between logo and email field
                    contentScale = ContentScale.Fit
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.width(240.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,    // White border when focused
                        unfocusedBorderColor = Color.White,  // White border when not focused
                        //  textColor = Color.White,             // Text color white
                        cursorColor = Color.White,           // Cursor color white
                        focusedLabelColor = Color.White      // Label color when focused
                    ),
                    textStyle = LocalTextStyle.current.copy(color = Color.White),

                    isError = errorMessage.isNotEmpty()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.width(240.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,    // White border when focused
                        unfocusedBorderColor = Color.White,  // White border when not focused

                        cursorColor = Color.White,           // Cursor color white
                        focusedLabelColor = Color.White      // Label color when focusedunfocusedLabelClor=Color.White
                    ),
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    trailingIcon = {
                        val icon =
                            if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = icon,
                                contentDescription = "Toggle Password Visibility"
                            )
                        }
                    },
                    isError = errorMessage.isNotEmpty()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            if (validateCredentials(email, password, validEmail, validPassword)) {
                                navController.navigate("dashboard")
                            } else {
                                errorMessage = "Invalid email or password"
                                snackbarHostState.showSnackbar("Invalid email or password")
                            }
                        }
                    },
                    modifier = Modifier.width(220.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF007BFF),
                    contentColor = Color.White)

                ) {
                    Text(text = "Login", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
// Simple email and password validation function
suspend fun validateCredentials(email: String, password: String, validEmail: String, validPassword: String): Boolean {
    // Simulate a network delay
    kotlinx.coroutines.delay(500)
    return email == validEmail && password == validPassword
}