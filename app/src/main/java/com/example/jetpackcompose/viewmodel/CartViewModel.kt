package com.example.jetpackcompose.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.data.model.Cart
import com.example.jetpackcompose.data.model.User
import com.example.jetpackcompose.data.network.RetrofitClient
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    private val _cart = MutableLiveData<Cart>()
    val cart: LiveData<Cart> get() = _cart

    fun fetchCart(id: Int) {
        viewModelScope.launch {
            try {
                println("salut !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                val response = RetrofitClient.apiService.getCart(id.toString())
                Log.i("response", response.toString())
                // On met à jour la cart
                _cart.value = response.body()
                Log.d("cart", _cart.value.toString())
            } catch (e: Exception) {
                // Gérer l'erreur
            }
        }
    }
}

