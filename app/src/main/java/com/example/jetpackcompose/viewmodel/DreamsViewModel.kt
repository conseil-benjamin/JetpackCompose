package com.example.jetpackcompose.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.example.jetpackcompose.data.model.Dream
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DreamsViewModel : ViewModel() {
    private var _dreamsList = MutableStateFlow<List<Dream>>(emptyList())
    var dreamList = _dreamsList.asStateFlow()

    private var _dream = MutableStateFlow<Dream?>(null)
    var dream = _dream.asStateFlow()


    init {
        getDreams()
    }

    fun getDreams() {
        val db = FirebaseFirestore.getInstance()

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

    fun addDream(title: String, content: String, date: String) {
        val db = FirebaseFirestore.getInstance()

        val dreamToAdd = Dream(
            title = title,
            content = content,
            date = date
        )

        db.collection("dreams")
            .add(dreamToAdd)
            .addOnSuccessListener { documentReference ->
                println("Le rêve a été ajouté avec l'ID : ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Erreur lors de l'ajout du rêve : $e")
            }
    }

}