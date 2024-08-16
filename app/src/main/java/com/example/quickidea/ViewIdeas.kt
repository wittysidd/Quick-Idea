package com.example.quickidea

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.quickidea.ui.theme.comfortaFont

@Composable
fun ViewIdeaScreen(navController: NavController, drawerState: DrawerState){

    val viewModel = myViewModel()
    val listOfIdeas = remember { mutableStateListOf<myViewModel.TheIdea>() }

    viewModel.viewIdeas(listOfIdeas)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomAppBar(drawerState = drawerState, title = "Your Ideas" )
        Text(text = "Tap for description !" ,modifier = Modifier
            .padding(0.dp, 20.dp, 0.dp, 15.dp)
            .fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 25.sp, fontFamily = comfortaFont,color = Color.Black)
        LazyColumn {
            items(listOfIdeas){
                IdeaCard(idea = it)
            }
        }
    }
}

@Composable
fun IdeaCard(idea: myViewModel.TheIdea){
    var expanded by remember { mutableStateOf(false) }

    Card( modifier = Modifier
        .padding(vertical = 8.dp)
        .fillMaxWidth(0.9f)
        .clip(RoundedCornerShape(8.dp))
        .clickable { expanded = !expanded }
        .animateContentSize(tween(1000))) {
        Text(text = idea.title, modifier = Modifier.padding(10.dp), fontFamily = comfortaFont, fontWeight = FontWeight.Bold, color = Color.Black)
        if(expanded) {
            Text(text = idea.description, modifier = Modifier.padding(10.dp), color = Color.Black, fontFamily = comfortaFont)
        }
    }
}