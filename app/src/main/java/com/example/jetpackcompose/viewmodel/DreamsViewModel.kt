package com.example.jetpackcompose.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.jetpackcompose.data.model.Dream
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.utils.SnackbarManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DreamsViewModel : ViewModel() {
    private var _dreamsList = MutableStateFlow<List<Dream>>(emptyList())
    var dreamList = _dreamsList.asStateFlow()

    private var _dream = MutableStateFlow<Dream?>(null)
    var dream = _dream.asStateFlow()

    suspend fun init() {
        getDreams()
    }

    fun getDreams(): Boolean {
        val db = FirebaseFirestore.getInstance()

        return try {
            db.collection("dreams")
                .addSnapshotListener { snapshots, exception ->
                    if (exception != null) {
                        println("Error getting documents: $exception")
                        return@addSnapshotListener
                    }

                    if (snapshots != null) {
                        val dreams = snapshots.map { document ->
                            // Convertir chaque document en un objet Dream
                            val dream = document.toObject(Dream::class.java)
                            dream.copy(id = document.id)  // Ajout de l'ID du document à l'objet Dream
                        }.filterNotNull()  // Évitez les éléments nulls

                        _dreamsList.value = dreams
                        Log.i("dreams", dreams.toString())
                    }
                }
            true
        } catch (e: Exception) {
            println("Erreur lors de l'ajout du rêve : $e")
            false
        }
    }

    fun getDreamById(idDocument: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("dreams")
            .document(idDocument)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    _dream.value = document.toObject(Dream::class.java)

                } else {
                    println("Aucun rêve trouvé avec cet ID")
                }
            }
            .addOnFailureListener { exception ->
                println("Erreur lors de la récupération du rêve: $exception")
            }
    }

    suspend fun addDream(title: String, content: String, date: String): Boolean {
        val db = FirebaseFirestore.getInstance()

        val dreamToAdd = Dream(
            title = title,
            content = content,
            date = date
        )

        return try {
            db.collection("dreams")
                .add(dreamToAdd)
                .await() // Attend que l'opération se termine
            println("Le rêve a été ajouté avec succès.")
            true
        } catch (e: Exception) {
            println("Erreur lors de l'ajout du rêve : $e")
            false
        }
    }

    fun addDreamAndShowSnackbar(title: String, content: String, date: String) {
        viewModelScope.launch {
            val isAdded = addDream(title, content, date)
            if (isAdded) {
                SnackbarManager.showMessage("Rêve ajouté avec succès.")
            } else {
                SnackbarManager.showMessage("Erreur lors de l'ajout du rêve.")
            }
        }
    }

    fun getDreamsAndShowSnackbar() {
        viewModelScope.launch {
            val getDreams = getDreams()
            Log.i("getDreams", getDreams.toString())
            if (getDreams) {
                SnackbarManager.showMessage("Rêves récupérés avec succès.")
            } else {
                SnackbarManager.showMessage("Erreur lors de la récupération des rêves.")
            }
        }
    }

    fun updateDream () {
        // TODO : faire une dialogue pour get les infos du rêve à modifier et update les changements
    }

}