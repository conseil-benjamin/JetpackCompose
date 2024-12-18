package com.example.jetpackcompose

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun Profil(navController: NavController) {
    Column {
        Text(text = "Profil utilisateur")
        Button(onClick = {
            navController.navigate("screen_two")
        }) {
            Text(text = "Aller à l'Écran 2")
        }
    }
}
