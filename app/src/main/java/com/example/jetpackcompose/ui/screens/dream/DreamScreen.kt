package com.example.jetpackcompose.ui.screens.dream

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose.viewmodel.DreamsViewModel

@Composable
fun DreamScreen (viewModel: DreamsViewModel = viewModel(), navController: NavHostController, dreamId : String){
    val dream by viewModel.dream.collectAsStateWithLifecycle()

    LaunchedEffect(dreamId) { // equivalent à un useEffect en React
        viewModel.getDreamById(dreamId)
    }

    Column(
        modifier = Modifier.
            padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (dream != null) {
            Text(
                text = dream!!.title,
            )
            Text(
                text = dream!!.content
            )
            Text(
                text = dream!!.id
            )
        }
    }
}