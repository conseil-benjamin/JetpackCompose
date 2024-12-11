package com.example.jetpackcompose.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.data.model.User
import com.example.jetpackcompose.data.network.RetrofitClient
import com.example.jetpackcompose.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    fun fetchUsers() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getUsers()
                Log.i("response", response.toString())
                // On met à jour la liste des utilisateurs
                _users.value = response.users.map { User(it.id, it.firstName, it.email) }
            } catch (e: Exception) {
                // Gérer l'erreur
                _users.value = emptyList() // Optionnel
            }
        }
    }
}

