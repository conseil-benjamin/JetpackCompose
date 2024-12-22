package com.example.jetpackcompose.ui.screens.dreams

    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.unit.dp
    import androidx.lifecycle.compose.collectAsStateWithLifecycle
    import androidx.lifecycle.viewmodel.compose.viewModel
    import com.example.jetpackcompose.viewmodel.DreamsViewModel
    import androidx.compose.runtime.getValue
    import androidx.compose.foundation.lazy.items
    import androidx.compose.material3.Button
    import androidx.compose.material3.SnackbarHost
    import androidx.compose.material3.SnackbarHostState
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.sp
    import androidx.navigation.NavHostController
    import com.example.jetpackcompose.ui.components.DialogCreateDream
    import androidx.compose.runtime.*
    import com.example.jetpackcompose.utils.SnackbarManager

    @Composable
    fun DreamListScreen(viewModel: DreamsViewModel = viewModel(), navController: NavHostController) {
        val dreams by viewModel.dreamList.collectAsStateWithLifecycle()
        var showDialog by remember { mutableStateOf(false) }

        val snackbarHostState = remember { SnackbarHostState() }

        // Ecoute des messages du SnackbarManager
        LaunchedEffect(Unit) { // unit veut dire que l'effet sera lancé une seule fois
            SnackbarManager.snackbarMessages.collect { snackbarMessage ->
                snackbarHostState.showSnackbar(
                    message = snackbarMessage.message,
                    actionLabel = snackbarMessage.actionLabel
                )
            }
        }

        Scaffold (
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ){ paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                LazyColumn(
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(dreams) { dream ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                                .clickable {
                                    viewModel.getDreamById(dream.id)
                                    navController.navigate("Dream/${dream.id}")
                                },
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = dream.title,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = dream.date,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = dream.content,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    item {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            onClick = { showDialog = true }) {
                            Text(text = "Ajouter un nouveau rêve")
                        }
                        if (showDialog) {
                            DialogCreateDream(
                                onDismiss = { showDialog = false },
                                onConfirm = {
                                        title, content, date ->
                                    viewModel.addDreamAndShowSnackbar(title, content, date)
                                    showDialog = false
                                }
                            )
                        }
                    }
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = { viewModel.getDreamsAndShowSnackbar() }
                ) {
                    Text(text = "Rafraichir")
                }
            }
        }
    }