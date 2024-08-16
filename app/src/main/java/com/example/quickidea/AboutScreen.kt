package com.example.quickidea

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope


@Composable
fun AboutScreen(drawerState: DrawerState, coroutineScope: CoroutineScope) {
    val isBackButtonDisabled = remember { mutableStateOf(true) }
    BackHandler(isBackButtonDisabled.value) {}
    Scaffold(
        topBar = { CustomAppBar(
            drawerState = drawerState,
            title = "About"
        ) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "About? #SD ")
        }
    }
}
