package com.example.quickidea

import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quickidea.ui.theme.comfortaFont
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SetupNavGraph(
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed) ,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val isBackButtonDisabled = remember { mutableStateOf(true) }
    BackHandler(isBackButtonDisabled.value) {}

    val currentScreen = remember { mutableStateOf("") }
    val gestureEnabled = !(currentScreen.value == "login_screen" || currentScreen.value == "splash_screen" ||currentScreen.value == "forgot_pwd_screen" )
    val context = LocalContext.current
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gestureEnabled,        // if it is login screen it returns false,
        drawerContent = {                                           // hence we cant access drawer on login screen

            ModalDrawerSheet {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier.size(150.dp),
                            imageVector = Icons.Filled.AccountCircle,
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    NavigationDrawerItem(
                        label = { Text("Add Idea", color = Color.Black) },
                        selected = false,
                        icon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            navController.navigate("home_screen")
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Settings", color = Color.Black) },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            navController.navigate("settings_screen")
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("About", color = Color.Black) },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            navController.navigate("about_screen")
                        }
                    )

                    NavigationDrawerItem(
                        label = { Text("Sign Out", color = Color.Black) },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }

                            val googleSignInClient =
                                GoogleSignIn.getClient(context, GoogleSignInOptions.DEFAULT_SIGN_IN)
                            googleSignInClient.signOut().addOnCompleteListener {
                                Firebase.auth.signOut()
                                navController.navigate("login_screen")
                            }

                        }
                    )
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "splash_screen"
        ) {
            composable(
                route = "splash_screen",
            ) {
                currentScreen.value = "splash_screen"
                SplashScreen(navController = navController)
            }
            composable(
                route = "login_screen"
            ) {
                currentScreen.value = "login_screen"
                Login(navController = navController)
            }
            composable(
                route = "home_screen"
            ) {
                currentScreen.value = "home_screen"
                HomeScreen(navController = navController, drawerState)
            }

            composable(
                route = "view_ideas_screen"
            ) {
                currentScreen.value = "view_ideas_screen"
                ViewIdeaScreen(navController = navController, drawerState = drawerState)
            }

            composable(
                route = "settings_screen"
            ) {
                currentScreen.value = "settings_screen"
                SettingsScreen(drawerState, coroutineScope)
            }

            composable(
                route = "about_screen"
            ) {
                currentScreen.value = "about_screen"
                AboutScreen(drawerState, coroutineScope)
            }
            composable(
                route = "forgot_pwd_screen"
            ) {
                currentScreen.value = "forgot_pwd_screen"
                ForgotPasswordScreen(navController)
            }
        }
    }
}