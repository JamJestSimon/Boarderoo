package pl.boarderoo.mobileapp.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ActionCodeInfo
import kotlinx.coroutines.launch
import pl.boarderoo.mobileapp.LightButton
import pl.boarderoo.mobileapp.R
import pl.boarderoo.mobileapp.datastore.saveLoginData
import pl.boarderoo.mobileapp.start.StartActivity
import pl.boarderoo.mobileapp.ui.theme.BoarderooMobileAppTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoarderooMobileAppTheme {
                Surface(
                    color = colorResource(R.color.backgroundColor)
                ) {
                    val context = LocalContext.current
                    val navController = rememberNavController()
                    var selectedRoute by remember { mutableStateOf("Main Screen") }
                    val scope = rememberCoroutineScope()
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet {
                                Text(
                                    "Witaj, ${AppRuntimeData.user?.name}",
                                    modifier = Modifier.padding(16.dp)
                                )
                                HorizontalDivider()
                                NavigationDrawerItem(
                                    label = { Text("Strona Główna") },
                                    selected = selectedRoute == "MainScreen",
                                    onClick = {
                                        selectedRoute = "MainScreen"
                                        navController.navigate("MainScreen")
                                    }
                                )
                                NavigationDrawerItem(
                                    label = { Text("Gry") },
                                    selected = selectedRoute == "GameScreen",
                                    onClick = {
                                        selectedRoute = "GameScreen"
                                        navController.navigate("GameScreen")
                                    }
                                )
                                NavigationDrawerItem(
                                    label = { Text("Zamówienia") },
                                    selected = selectedRoute == "OrderScreen",
                                    onClick = {
                                        selectedRoute = "OrderScreen"
                                        navController.navigate("OrderScreen")
                                    }
                                )
                                NavigationDrawerItem(
                                    label = { Text("Koszyk") },
                                    selected = selectedRoute == "CartScreen",
                                    onClick = {
                                        selectedRoute = "CartScreen"
                                        navController.navigate("CartScreen")
                                    }
                                )
                                NavigationDrawerItem(
                                    label = { Text("Konto") },
                                    selected = selectedRoute == "AccountInfo",
                                    onClick = {
                                        selectedRoute = "AccountInfo"
                                        navController.navigate("AccountInfo")
                                    }
                                )
                                Box(
                                    modifier = Modifier
                                        .weight(1.0f)
                                        .fillMaxSize()
                                ) {
                                    Box(
                                        modifier = Modifier.align(Alignment.BottomEnd)
                                    ) {
                                        LightButton(
                                            text = "Wyloguj",
                                            fontSize = 16.sp
                                        ) {
                                            AppRuntimeData.user = null
                                            scope.launch {
                                                saveLoginData(context, false, "")
                                            }
                                            context.startActivity(
                                                Intent(
                                                    context,
                                                    StartActivity::class.java
                                                )
                                            )
                                            (context as Activity).finish()
                                        }
                                    }
                                }
                            }
                        }
                    ) {
                        Scaffold(
                            topBar = {
                                CenterAlignedTopAppBar(
                                    title = { Text(selectedRoute) },
                                    navigationIcon = {
                                        IconButton(
                                            onClick = {
                                                scope.launch {
                                                    drawerState.apply {
                                                        if (isClosed) open() else close()
                                                    }
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Menu,
                                                contentDescription = "DrawerIcon"
                                            )
                                        }
                                    }
                                )
                            },
                            containerColor = colorResource(R.color.backgroundColor)
                        ) { paddingValues ->
                            Box(modifier = Modifier.padding(paddingValues)) {
                                NavHost(
                                    navController = navController,
                                    startDestination = "MainScreen"
                                ) {
                                    composable(route = "MainScreen") { MainScreen() }
                                    composable(route = "GameScreen") { GameScreen() }
                                    composable(route = "OrderScreen") { OrderScreen() }
                                    composable(route = "CartScreen") { OrderScreen() }
                                    composable(route = "AccountInfo") { AccountInfo() }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}