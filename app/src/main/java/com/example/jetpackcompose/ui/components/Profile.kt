package com.example.jetpackcompose.ui.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun Profile(navController: NavHostController) {
    // Affichage du profil utilisateur
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    val username = sharedPreferences.getString("displayName", "unknow user")
    val email = sharedPreferences.getString("email", "unknow email")
    val photoUrl = sharedPreferences.getString("photoUrl", "unknow photo")

    Column(
        modifier = Modifier.
            fillMaxSize()
            .padding(10.dp)
    ) {
        Text(text = "Profil utilisateur")
        if (photoUrl != null && photoUrl.isNotEmpty()) {
            Image(
                painter = rememberImagePainter(photoUrl),
                contentDescription = "Photo de profil"
            )
        }        
        Text(text = "Bienvenue : $username")
        Text(text = "Email: $email")
        Button(onClick = {
            navController.navigate("DreamList")
        }) {
            Text(text = "Aller à l'Écran 2")
        }
    }
}