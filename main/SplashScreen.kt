package com.example.nokianew


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import androidx.navigation.compose.rememberNavController


@Composable
fun SplashScreen(navController: NavHostController) {
    // Animation for scaling the Nokia image
    // Animation for glowing effect (fading in and out)
    val glowAlpha = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }

    // Triggered when the SplashScreen composable is launched
    LaunchedEffect(Unit) {
        glowAlpha.animateTo(
            targetValue = 5000f,  // Fade in
            animationSpec = tween(durationMillis = 1000)  // Duration of fade-in
        )
        delay(500)  // Keep it visible for a bit
        glowAlpha.animateTo(
            targetValue = 0f,  // Fade out
            animationSpec = tween(durationMillis = 700)  // Duration of fade-out
        )
        // Wait for a slight pause before starting the scale animation
        delay(1000)  // Adjust as needed for the timing
        scale.animateTo(
            targetValue = 0.3f,  // Shrink the image
            animationSpec = tween(durationMillis = 800)  // Duration of the animation
        )


        navController.navigate("login") {
            popUpTo("splash_screen") { inclusive = true }
        }
    }

    // UI Composition
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Background image (optional)
        Image(
            painter = painterResource(id = R.drawable.img),  // Your background image resource
            contentDescription = null,
            contentScale = ContentScale.Crop,  // This scales the image to fill the container while cropping it if necessary
            modifier = Modifier.fillMaxSize()
        )

        // Nokia logo image with scaling animation
        Image(
            painter = painterResource(id = R.drawable.logo2),  // Your Nokia logo image resource
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)  // Initial size
                .scale(scale.value)  // Apply the scaling animation here
        )
    }

}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController())
}