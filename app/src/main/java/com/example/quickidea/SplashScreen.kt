package com.example.quickidea

import android.os.Build.VERSION.SDK_INT
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.quickidea.ui.theme.comfortaFont
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SplashScreen(navController: NavController) {
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.7f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            alpha.animateTo(1f, animationSpec = tween(500))
            scale.animateTo(1f, animationSpec = tween(durationMillis = 1000))
            delay(200) // 2 seconds delay
            navController.navigate("login_screen") {
                popUpTo("splash_screen") {
                    inclusive = true
                } // Remove splash screen from back stack
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFDD835), // Start color
                        Color(0xFFFB8C00)  // End color
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        val fontFamilies = listOf(
            FontFamily.Default,
            FontFamily.Serif,
            FontFamily.Monospace,
            FontFamily.Cursive,
            comfortaFont
            // Add more custom fonts if needed
        )

        // State to hold the current font index
        var fontIndex by remember {
            mutableStateOf(0)
        }

        // Use LaunchedEffect to animate the font change
        LaunchedEffect(Unit) {
            while (true) {
                delay(200) // Change font every 100ms
                fontIndex = (fontIndex + 1) % fontFamilies.size
            }
        }

        Column(modifier =Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
            ) {

            // animated GIF
            AnimatedGIF()

            Text(
                text = "Quick Idea",
                color = Color.Black,
                fontFamily = comfortaFont,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .graphicsLayer(
                        alpha = alpha.value,
                        scaleX = scale.value,
                        scaleY = scale.value
                    )

            )


        }


    }
}

@Composable
fun AnimatedGIF(){
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components{
            if(SDK_INT >=28){
                add(ImageDecoderDecoder.Factory())
            }else{
                add(GifDecoder.Factory())
            }
        }.build()

    val painter = rememberImagePainter(
        data = R.drawable.energy_icon,
        imageLoader = imageLoader)

    Image(painter = painter,
        contentDescription ="",
        contentScale = ContentScale.Fit
    )
}