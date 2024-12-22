package com.example.jetpackcompose.utils

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object SnackbarManager {
    private val snackbarChannel = Channel<SnackbarMessage>()
    val snackbarMessages = snackbarChannel.receiveAsFlow()

    suspend fun showMessage(message: String, actionLabel: String? = null) {
        snackbarChannel.send(SnackbarMessage(message, actionLabel))
    }
}

data class SnackbarMessage(val message: String, val actionLabel: String? = null)