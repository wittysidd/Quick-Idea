package com.example.quickidea

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quickidea.ui.theme.bounceClick
import com.example.quickidea.ui.theme.bounceClickEffect
import com.example.quickidea.ui.theme.comfortaFont
import com.example.quickidea.ui.theme.pressClickEffect
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController : NavController,drawerState: DrawerState?) {
    val isBackButtonDisabled = remember { mutableStateOf(true) }
    BackHandler(isBackButtonDisabled.value) {}

    var viewModel = myViewModel()
    val context = LocalContext.current

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val color1 by infiniteTransition.animateColor(
        initialValue = Color(0xFF021633),
        targetValue = Color(0xFF62EEF3),
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )


    val color2 by infiniteTransition.animateColor(
        initialValue = Color(0xFF6AE6EB),
        targetValue = Color(0xFF041735),
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val colorOfButton1 by infiniteTransition.animateColor(
        initialValue = Color(0xFFFFD180),
        targetValue = Color(0xFFB33F00),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    val colorOfButton2 by infiniteTransition.animateColor(
        initialValue = Color(0xFFBE4300),
        targetValue = Color(0xFFFFD180),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val viewIdeaBtnColor1 by infiniteTransition.animateColor(
        initialValue = Color(0xFF880E4F),
        targetValue = Color(0xFFF9A825),
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    listOf(color1, color2)
                )
            )
    ) {
        val coroutineScope = rememberCoroutineScope()

        // MENU ICON ______________________________________________
        IconButton(onClick = {
            coroutineScope.launch {
                drawerState?.open()
            }
        },
            modifier = Modifier.size(40.dp,40.dp).padding(10.dp,15.dp,0.dp,0.dp)) {
            Icon(Icons.Filled.Menu, contentDescription = "", modifier = Modifier.size(30.dp,30.dp), tint = Color.White)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 50.dp, 0.dp, 0.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Save your Idea !",
                modifier = Modifier.padding(0.dp, 15.dp, 0.dp, 0.dp),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = comfortaFont,
                color = Color.White
            )
        }



        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            var display = remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .size(300.dp, 300.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { display.value = true },
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp
                    ),
                    modifier = Modifier
                        .size(250.dp) // Adjust size as needed
                        .shadow(10.dp, CircleShape) // Shadow for 3D effect

                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        colorOfButton1,
                                        colorOfButton2
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            color = Color.White,
                            fontSize = 100.sp, // Adjust size as needed
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            if (display.value) {
                AddIdea(display = display)
            }

        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 0.dp, 0.dp, 25.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ElevatedButton(
                onClick = { navController.navigate("view_ideas_screen") },
                modifier = Modifier.pressClickEffect(),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = viewIdeaBtnColor1
                )
            ) {
                Text(
                    "View Ideas",
                    fontFamily = comfortaFont,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddIdea(display: MutableState<Boolean>) {
    var ideatitle by remember { mutableStateOf("") }
    var ideaDescription by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(display) }
    val context = LocalContext.current
    val viewModel = myViewModel()


    if(showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            text = {
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = "Enter Title:", fontSize = 17.sp, fontWeight = FontWeight.Bold, fontFamily = comfortaFont,color = Color.Black)
                    OutlinedTextField(
                        value = ideatitle,
                        onValueChange = { ideatitle = it },
                        label = {Text("Idea Name", fontFamily = comfortaFont,color = Color.Black)},
                        singleLine = true,
                        textStyle = TextStyle(fontFamily = comfortaFont, fontWeight = FontWeight.Bold, color = Color.Black),
                        shape = RoundedCornerShape(40.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            cursorColor = Color.Black,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Black,)
                    )

                    Spacer(modifier = Modifier.padding(5.dp))

                    Text(text = "Enter idea description:", fontSize = 17.sp, fontFamily = comfortaFont,color = Color.Black)
                    OutlinedTextField(
                        value = ideaDescription,
                        onValueChange = { ideaDescription = it },
                        label = {Text("Details", fontFamily = comfortaFont,color = Color.Black)},
                        modifier = Modifier.size(250.dp,200.dp),
                        textStyle = TextStyle(fontFamily = comfortaFont, fontWeight = FontWeight.Bold,color = Color.Black),
                        colors = OutlinedTextFieldDefaults.colors(
                            cursorColor = Color.Black,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Black,)
                    )
                }

            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { showDialog.value = false }) {
                        Text("Close")
                    }
                    Button(onClick = {
                        showDialog.value = false
                        viewModel.saveIdea(ideatitle,ideaDescription)
                        Toast.makeText(context, "Idea Saved !", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Save")
                    }
                }
            }

        )
    }
}
