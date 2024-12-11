package com.example.jetpackcompose

import android.R
import android.os.Bundle
import android.util.Log
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
import androidx.compose.material3.Button
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
import com.example.jetpackcompose.ui.viewmodel.CartViewModel


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
            Image(painter = painterResource(id = R.drawable.ic_delete),
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