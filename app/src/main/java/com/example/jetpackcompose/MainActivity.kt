package com.example.jetpackcompose

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.jetpackcompose.ui.theme.JetpackComposeTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcompose.data.model.User
import com.example.jetpackcompose.data.repository.UserRepository
import com.example.jetpackcompose.ui.viewmodel.UserViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.jetpackcompose.ui.viewmodel.CartViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.security.MessageDigest
import java.util.UUID


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetpackComposeTheme {
                AppNavigator()
            }
        }
    }
}

@Composable
fun AppNavigator() {
    // Création du navController
    val navController = rememberNavController()
    NavHost(navController, startDestination = "list") {
        composable("counter") { CounterScreen(navController) }
        composable("details") { DetailsScreen(navController) }
        composable("list") { ListUser(navController = navController) }
        composable("cart") { ListCart(navController = navController, id = 1) }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.Blue)
            .size(400.dp)
    ){
        lazyColumn()
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Hello $name!",
                modifier = Modifier
                    .background(Color.Red)
                    .background(Color.Green),
                color = Color.Blue,
                fontSize = 30.sp,
            )
            Text(
                text = "Salut !",
                fontSize = 30.sp
            )
        }
        Text(
            text = "Salut 2 !",
            modifier = Modifier
                .padding(15.dp),
            color = Color.White,
        )
        for (i in 1..10){
            Image(painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .background(Color.Black)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun CounterScreen(navController: NavHostController) {
    val count = remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Count: ${count.value}", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { count.value++ }) {
            Text(text = "Increment")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("list") }) {
            Text(text = "Go to User List")
        }
    }
}


@Composable
fun ListUser(viewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), navController: NavHostController) {
    val usersState = viewModel.users.observeAsState(emptyList())
    val users = usersState.value

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { viewModel.fetchUsers() }) {
            Text(text = "Fetch Users")
            Log.i("test", "test")
        }

        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(users.size) { index ->
                val user = users[index]  // Récupérer l'utilisateur avec l'index

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("cart")
                        }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${user.id} ${user.firstName} - ${user.email}",
                        fontSize = 20.sp,
                        modifier = Modifier.weight(1f) // Pour répartir l'espace
                    )
                }
            }
        }


        Button(onClick = { navController.navigate("details") }) {
            Text(text = "Go to details")
        }
    }
}

@Composable
fun ListCart(viewModel: CartViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), navController: NavHostController, id: Int) {
    val cartState = viewModel.cart.observeAsState()
    val cart = cartState.value

    viewModel.fetchCart(id)

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        LazyColumn {
            items(cart?.products?.size ?: 0) { index ->
                val product = cart?.products?.get(index)
                Text(text = "${product?.title} - ${product?.price} €")
            }
            item {
                Text(text = "Total: ${cart?.total} €")
            }
            item {
                Text(text = "Total products: ${cart?.totalProducts}")
            }
            item {
                Text(text = "Discounted total: ${cart?.discountedTotal} €")
            }
            item {
                Button(onClick = { navController.navigate("details") }) {
                    Text(text = "Go to details")
                }
            }
            item {
                Button(onClick = { navController.popBackStack() }) {
                    Text(text = "Back to Counter")
                }
            }
        }
    }
}


@Composable
fun lazyColumn () {
    LazyColumn {
        items(5){ index ->
            Text(
                text = "Salut $index"
            )
        }
    }
}

@Composable
fun DetailsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Details Screen", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Back to Counter")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetpackComposeTheme {
        Greeting("Android")
    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    JetpackComposeTheme {
        LoginScreen()
    }
}

@Composable
fun LoginScreen (modifier: Modifier = Modifier) {

    val password = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }

    Column (modifier = modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(20.dp)
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Sign-in",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Please fill the form to continue",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            placeholder = {
                Text(
                    text = "Email"
                )
            },
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.Email, contentDescription = null)
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            placeholder = {
                Text(
                    text = "Password"
                )
            },
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.Lock, contentDescription = null)
            },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Sign-in",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

class AuthenticationManager(val context: Context) {
    private val auth = Firebase.auth

    fun createAccountWithEmail(email: String, password: String): Flow<AuthResponse> = callbackFlow {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthResponse.Success)
                } else {
                    trySend(AuthResponse.Error(message = task.exception?.message ?: ""))
                }
            }
        awaitClose()
    }

    fun loginWithEmail(email: String, password: String): Flow<AuthResponse> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(AuthResponse.Success)
                } else {
                    trySend(AuthResponse.Error(message = task.exception?.message ?: ""))
                }
            }
        awaitClose()
    }

    private fun createNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)

        return digest.fold("") { str, it ->
            str + "%02x".format(it)
        }
    }

    fun signInWithGoogle(): Flow<AuthResponse> = callbackFlow {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_client_id))
            .setAutoSelectEnabled(false)
            .setNonce(createNonce())
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            val credential = result.credential
            if (credential is CustomCredential) {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)

                        val firebaseCredential = GoogleAuthProvider
                            .getCredential(
                                googleIdTokenCredential.idToken,
                                null
                            )

                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    trySend(AuthResponse.Success)
                                } else {
                                    trySend(AuthResponse.Error(message = it.exception?.message ?: ""))
                                }
                            }

                    } catch (e: Exception) {
                        trySend(AuthResponse.Error(message = e.message ?: ""))
                    }
                }
            }
        } catch (e: Exception){
            trySend(AuthResponse.Error(message = e.message ?: ""))
        }
        awaitClose()
    }
}

interface AuthResponse {
    data object Success: AuthResponse
    data class Error(val message: String): AuthResponse
}