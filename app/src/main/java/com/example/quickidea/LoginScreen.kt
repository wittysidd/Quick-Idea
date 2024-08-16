package com.example.quickidea

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseInCirc
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quickidea.ui.theme.comfortaFont
import com.example.quickidea.ui.theme.pressClickEffect
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


@Composable
fun Login(navController: NavController) {



    val isBackButtonDisabled = remember { mutableStateOf(true) }
    BackHandler(isBackButtonDisabled.value) {}
    val auth = Firebase.auth
    var user by remember { mutableStateOf(auth.currentUser) }
    val token = stringResource(id = R.string.client_id)
    val context = LocalContext.current
    var enabled by remember { mutableStateOf(true) }
    var loading by remember { mutableStateOf(false) }


    // sign in code --------------------------------------------------------------------------------------------------------
    val launcher = rememberFirebaseAuthLauncher(onAuthComplete = { result ->
        user = result.user
        Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
        loading = false
    }) {
        user = null
        loading = false
        enabled = true
    }

    // sign in code --------------------------------------------------------------------------------------------------------

    var email by remember { mutableStateOf("") }
    var pwd by remember { mutableStateOf("") }


    val infiniteTransition = rememberInfiniteTransition(label = "")

    val signUpText = remember { mutableStateOf("Sign In with email") }
    val signInButtonText = remember { mutableStateOf("Sign In") }
    val newUserState = remember { mutableStateOf("New User?") }
    val forgetPassword = remember { mutableStateOf(false) }

    var clicked by remember { mutableStateOf(false) }

    val signUpColor = remember { Animatable(Color.Black) }
    val buttonTextColor = remember { Animatable(Color.Black) }

    LaunchedEffect(clicked) {
        buttonTextColor.animateTo(if(clicked) Color.Black else Color.White)
        signUpColor.animateTo(if (clicked) Color.White else Color.Black)
    }
    if (clicked){
        signUpText.value = "Sign Up with email"
        signInButtonText.value = "Sign Up"
        newUserState.value = "Old User?"
    }else {
        signUpText.value = "Sign In with email"
        signInButtonText.value = "Sign In"
        newUserState.value = "New User?"
    }

    val color1 by infiniteTransition.animateColor(
        initialValue = Color(0xFF8E24AA),
        targetValue = Color(0xFFFFB300),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "color1"
    )

    val color2 by infiniteTransition.animateColor(
        initialValue = Color(0xFFFFB300),
        targetValue = Color(0xFF8E24AA),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "color 2"
    )

    val colorOfNewUser by infiniteTransition.animateColor(
        initialValue = Color(0xFF001FFF),
        targetValue = Color(0xFF000000),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "color 2"
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(colors = listOf(color1, color2))),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (user == null) {
            Text(
                "Welcome to Quick Idea!",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontFamily = comfortaFont,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                color = signUpColor.value
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                signUpText.value,
                fontSize = 20.sp,
                fontFamily = comfortaFont,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                color = signUpColor.value
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                textStyle = TextStyle(
                    fontFamily = comfortaFont,
                    fontWeight = FontWeight.Bold,
                    color = signUpColor.value
                ),
                label = { Text("Email ID", color = signUpColor.value) },
                singleLine = true,
                enabled = enabled,
                modifier = Modifier.size(350.dp, 75.dp),
                shape = RoundedCornerShape(40.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = signUpColor.value,
                    focusedBorderColor = signUpColor.value,
                    unfocusedBorderColor = signUpColor.value,
                    focusedLabelColor = signUpColor.value,
                    unfocusedLabelColor = signUpColor.value,
                )
            )
            OutlinedTextField(
                value = pwd,
                onValueChange = { pwd = it },
                textStyle = TextStyle(
                    fontFamily = comfortaFont,
                    fontWeight = FontWeight.Bold,
                    color = signUpColor.value
                ),
                label = { Text("Password", color = signUpColor.value) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                enabled = enabled,
                modifier = Modifier.size(350.dp, 75.dp),
                shape = RoundedCornerShape(40.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = signUpColor.value,
                    focusedBorderColor = signUpColor.value,
                    unfocusedBorderColor = signUpColor.value,
                    focusedLabelColor = signUpColor.value,
                    unfocusedLabelColor = signUpColor.value,
                )
            )
            
            if(!clicked && forgetPassword.value){
                Text(text = "Forgot Password?", color = colorOfNewUser, modifier = Modifier
                    .padding(5.dp)
                    .clickable {
                        navController.navigate("forgot_pwd_screen")
                    })
            }

            var confirmPwd by remember { mutableStateOf("") }
            var matched by remember { mutableStateOf(false) }
            var mismatchColor by remember { mutableStateOf(Color.Black) }

            if (clicked) {
                AnimatedVisibility(
                    visible = clicked,
                    enter = fadeIn() + slideInVertically(tween(500)),
                    exit = fadeOut() + slideOutVertically(tween(1000))
                ) {
                    OutlinedTextField(
                        value = confirmPwd,
                        onValueChange = {
                            confirmPwd = it
                        },
                        textStyle = TextStyle(
                            fontFamily = comfortaFont,
                            fontWeight = FontWeight.Bold,
                            color = signUpColor.value
                        ),
                        label = { Text("Confirm Password", color = mismatchColor) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.size(350.dp, 75.dp),
                        enabled = enabled,
                        shape = RoundedCornerShape(40.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            cursorColor = signUpColor.value,
                            focusedBorderColor = mismatchColor,
                            unfocusedBorderColor = mismatchColor,
                            focusedLabelColor = mismatchColor,
                            unfocusedLabelColor = mismatchColor,
                        )
                    )
                }
            }
            // password matching conditions and animations ...
            if (clicked && pwd != confirmPwd) {
                mismatchColor = Color.Red
                matched = true
                Text("Password mismatch", color = Color.Red)
            } else {
                mismatchColor = signUpColor.value
            }
            if (clicked && confirmPwd.length < 8) {
                Text("Password must me 8 character long", color = Color.Red)
            }


            Spacer(modifier = Modifier.height(35.dp))
                ElevatedButton(onClick = {
                    if (!clicked) { // sign in  logic
                        if (email.isNotEmpty() && pwd.isNotEmpty()) {
                            enabled = false
                            loading = true
                            myViewModel().signIn(auth, email, pwd, navController,
                                onSuccess = {
                                    loading = false
                                    Toast.makeText(context, "Sign in Success!", Toast.LENGTH_SHORT).show()
                                },
                                verificationError = {
                                    clicked = false
                                    enabled = true
                                    loading = false
                                    Toast.makeText(context, "Please verify your mail!", Toast.LENGTH_SHORT).show()
                                },
                                verifyEmail = {
                                    clicked = false
                                    enabled = true
                                    loading = false
                                    Toast.makeText(context, "Please verify your mail!", Toast.LENGTH_SHORT).show()
                                },
                                onWrongEmail = {
                                    enabled = true
                                    loading = false
                                    Toast.makeText(context, "Invalid Email format", Toast.LENGTH_SHORT).show()
                                },
                                onWrongPassword = {
                                    forgetPassword.value = true
                                    enabled = true
                                    loading = false
                                    Toast.makeText(context, "Incorrect Email/Password", Toast.LENGTH_SHORT).show()
                                })
                        } else {
                            Toast.makeText(context, "Please Enter Email & password", Toast.LENGTH_SHORT).show()
                        }
                    } else {     // sign up logic
                        if (email.isNotEmpty() && pwd.isNotEmpty() && (confirmPwd.length > 7) && (matched)) {
                            enabled = false
                            loading = true
                            myViewModel().signUp(auth, email, confirmPwd,
                                OnSuccess = {
                                    clicked = false
                                    enabled = true
                                    loading = false
                                    Toast.makeText(context, "Verification Email Sent !", Toast.LENGTH_LONG).show()
                                },
                                OnSameEmail = {
                                    enabled = true
                                    loading = false
                                    Toast.makeText(context, "Account Already Exists", Toast.LENGTH_LONG).show()
                                },
                                onWrongEmail = {
                                    enabled = true
                                    loading = false
                                    Toast.makeText(context, "Invalid Email !", Toast.LENGTH_LONG).show()
                                })
                        } else {
                            Toast.makeText(context, "Enter valid Email & password", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                    enabled = enabled,
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = signUpColor.value,
                        contentColor = buttonTextColor.value
                    ),
                    modifier = Modifier
                        .pressClickEffect()
                        .size(135.dp, 55.dp)
                ) {
                    Text(signInButtonText.value, fontFamily = comfortaFont)
                }

                if (loading){
                    Progressing()
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = newUserState.value,
                    fontFamily = comfortaFont,
                    fontSize = 16.sp,
                    color = colorOfNewUser,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        clicked = !clicked
                        enabled = true
                        email = ""
                        pwd = ""
                        confirmPwd = ""
                    })

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    "------OR------", modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(), textAlign = TextAlign.Center, color = signUpColor.value
                )

                ElevatedButton(
                    onClick = {
                        enabled = false
                        loading = true
                        // sign in code --------------------------------------------------------------------------------------------------------
                        val gso = GoogleSignInOptions
                            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(token)
                            .requestEmail()
                            .build()

                        val googleSignInClient = GoogleSignIn.getClient(context, gso)
                        googleSignInClient.signOut().addOnCompleteListener {
                            launcher.launch(googleSignInClient.signInIntent)
                        }
                    },
                    // sign in code --------------------------------------------------------------------------------------------------------
                    shape = RoundedCornerShape(40.dp),
                    enabled = enabled,
                    modifier = Modifier
                        .padding(5.dp)
                        .size(280.dp, 55.dp)
                        .pressClickEffect(),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = signUpColor.value,
                        contentColor = buttonTextColor.value
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Sign in with Google ", fontFamily = comfortaFont)
                        Image(
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = "",
                            modifier = Modifier
                                .size(20.dp, 20.dp)
                                .padding(2.dp, 2.dp, 0.dp, 0.dp)
                        )
                    }


                }
            }else {
            navController.navigate("home_screen")
        }
        }
    }

@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()

    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            Log.d("GoogleAuth", "account: $account")

            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            scope.launch {
                val authResult = Firebase.auth.signInWithCredential(credential).await()
                onAuthComplete(authResult)
            }
        } catch (e: ApiException) {
            Log.d("GoogleAuth", "Error: $e")
            onAuthError(e)
        }
    }
}

@Composable
fun ForgotPasswordScreen(navController: NavController){
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf<Boolean?>(null) }

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val color1 by infiniteTransition.animateColor(
        initialValue = Color(0xFF000000),
        targetValue = Color(0xFF009399),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "color1"
    )

    val color2 by infiniteTransition.animateColor(
        initialValue = Color(0xFF009197),
        targetValue = Color(0xFF000000),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "color 2"
    )

    Column(
        modifier = Modifier.fillMaxSize().background(brush = Brush.linearGradient(colors = listOf(color1, color2))),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Text("Forgot Password", style = MaterialTheme.typography.headlineLarge, color = Color.White, fontFamily = comfortaFont)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            textStyle = TextStyle(
                fontFamily = comfortaFont,
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            label = { Text("Enter Email", color = Color.White) },
            singleLine = true,
            modifier = Modifier.size(350.dp, 75.dp),
            shape = RoundedCornerShape(40.dp),
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        val context = LocalContext.current
        Button(onClick = {
            if (email.isNotEmpty()) {
                myViewModel().sendPasswordResetEmail(email) { success, msg ->
                    isSuccess = success
                    message = msg ?: "Unknown error"
                }
            }else{
                Toast.makeText(context, "Enter your email !", Toast.LENGTH_LONG).show()
            }
        },
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )) {
            Text("Reset Password", fontFamily = comfortaFont, fontWeight = FontWeight.Bold, color = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = if (isSuccess == true) Color.Green else Color.Red,
                fontFamily = comfortaFont,
                fontSize = 15.sp,
                modifier = Modifier.padding(10.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun Progressing(){
    CircularProgressIndicator()
}