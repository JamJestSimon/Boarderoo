package pl.boarderoo.mobileapp.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import pl.boarderoo.mobileapp.LightButton
import pl.boarderoo.mobileapp.R
import pl.boarderoo.mobileapp.datastore.AppRuntimeData
import pl.boarderoo.mobileapp.datastore.getLoginState
import pl.boarderoo.mobileapp.datastore.getUserEmail
import pl.boarderoo.mobileapp.datastore.saveLoginData
import pl.boarderoo.mobileapp.start.StartActivity
import pl.boarderoo.mobileapp.ui.theme.BoarderooMobileAppTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MAIN ACTIVITY", "Reached")
        super.onCreate(savedInstanceState)
        setContent {
            val isLoggedIn by getLoginState(LocalContext.current).collectAsState(initial = false)
            val email by getUserEmail(LocalContext.current).collectAsState(initial = "")
            Log.d("DATASTORE", "isLoggedIn: $isLoggedIn")
            Log.d("DATASTORE", "email: $email")
            BoarderooMobileAppTheme {
                Surface(
                    color = colorResource(R.color.backgroundColor)
                ) {
                    val context = LocalContext.current
                    val navController = rememberNavController()
                    var selectedRoute by remember { mutableStateOf("Strona Główna") }
                    val scope = rememberCoroutineScope()
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet(
                                modifier = Modifier
                                    .padding(start = 0.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(
                                        2.dp,
                                        colorResource(R.color.buttonSecondColor),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .background(colorResource(R.color.textColor)),

                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .background(colorResource(id = R.color.textColor))
                                ) {
                                    Text(
                                        "Witaj, ${AppRuntimeData.user?.name}",
                                        modifier = Modifier
                                            .padding(16.dp),
                                        color = colorResource(R.color.backgroundColor)
                                    )
                                }
                                HorizontalDivider(
                                    color = colorResource(id = R.color.buttonSecondColor),
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .let {
                                            if (selectedRoute == "Strona Główna") {
                                                it.background(colorResource(R.color.backgroundColor))
                                            } else {
                                                it.background(colorResource(R.color.textColor))
                                            }
                                        }
                                        .clickable { // Akcja kliknięcia
                                            selectedRoute = "Strona Główna"
                                            navController.navigate("MainScreen")
                                            scope.launch {
                                                drawerState.apply {
                                                    if (isClosed) open() else close()
                                                }
                                            }
                                        }
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "Strona Główna",
                                        color = if (selectedRoute == "Strona Główna") {
                                            colorResource(R.color.textColor)
                                        } else {
                                            colorResource(R.color.backgroundColor)
                                        },
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .let {
                                            if (selectedRoute == "Gry") {
                                                it.background(colorResource(R.color.backgroundColor))
                                            } else {
                                                it.background(colorResource(R.color.textColor))
                                            }
                                        }
                                        .clickable {
                                            selectedRoute = "Gry"
                                            navController.navigate("GameScreen")
                                            scope.launch {
                                                drawerState.apply {
                                                    if (isClosed) open() else close()
                                                }
                                            }
                                        }
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "Gry",
                                        color = if (selectedRoute == "Gry") {
                                            colorResource(R.color.textColor)
                                        } else {
                                            colorResource(R.color.backgroundColor)
                                        },
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .let {
                                            if (selectedRoute == "Zamówienia") {
                                                it.background(colorResource(R.color.backgroundColor))
                                            } else {
                                                it.background(colorResource(R.color.textColor))
                                            }
                                        }
                                        .clickable {
                                            selectedRoute = "Zamówienia"
                                            navController.navigate("OrderScreen")
                                            scope.launch {
                                                drawerState.apply {
                                                    if (isClosed) open() else close()
                                                }
                                            }
                                        }
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "Zamówienia",
                                        color = if (selectedRoute == "Zamówienia") {
                                            colorResource(R.color.textColor)
                                        } else {
                                            colorResource(R.color.backgroundColor)
                                        },
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .let {
                                            if (selectedRoute == "Koszyk") {
                                                it.background(colorResource(R.color.backgroundColor))
                                            } else {
                                                it.background(colorResource(R.color.textColor))
                                            }
                                        }
                                        .clickable {
                                            selectedRoute = "Koszyk"
                                            navController.navigate("CartScreen")
                                            scope.launch {
                                                drawerState.apply {
                                                    if (isClosed) open() else close()
                                                }
                                            }
                                        }
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "Koszyk",
                                        color = if (selectedRoute == "Koszyk") {
                                            colorResource(R.color.textColor)
                                        } else {
                                            colorResource(R.color.backgroundColor)
                                        },
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .let {
                                            if (selectedRoute == "Konto") {
                                                it.background(colorResource(R.color.backgroundColor))
                                            } else {
                                                it.background(colorResource(R.color.textColor))
                                            }
                                        }
                                        .clickable {
                                            selectedRoute = "Konto"
                                            navController.navigate("AccountInfo")
                                            scope.launch {
                                                drawerState.apply {
                                                    if (isClosed) open() else close()
                                                }
                                            }
                                        }
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "Konto",
                                        color = if (selectedRoute == "Konto") {
                                            colorResource(R.color.textColor)
                                        } else {
                                            colorResource(R.color.backgroundColor)
                                        },
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1.0f)
                                        .fillMaxSize()
                                        .background(colorResource(R.color.textColor))
                                ) {
                                    Box(
                                        modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp)
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
                                    title = {
                                        Text(
                                            text = selectedRoute,
                                            color = colorResource(id = R.color.backgroundColor)
                                        )
                                    },
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
                                                contentDescription = "DrawerIcon",
                                                tint = colorResource(id = R.color.backgroundColor)
                                            )
                                        }
                                    },
                                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                        containerColor = colorResource(id = R.color.textColor),
                                        titleContentColor = colorResource(id = R.color.backgroundColor)
                                    )
                                )
                            },
                            containerColor = colorResource(R.color.backgroundColor)
                        ) { paddingValues ->
                            Box(modifier = Modifier.padding(paddingValues)) {
                                NavHost(
                                    navController = navController,
                                    startDestination = "MainScreen"
                                ) {
                                    composable(route = "MainScreen") {
                                        selectedRoute = "Strona Główna"
                                        MainScreen()
                                    }
                                    composable(route = "GameScreen") {
                                        selectedRoute = "Gry"
                                        GameScreen()
                                    }
                                    composable(route = "OrderScreen") {
                                        selectedRoute = "Zamówienia"
                                        OrderScreen()
                                    }
                                    composable(route = "CartScreen") {
                                        selectedRoute = "Koszyk"
                                        CartScreen()
                                    }
                                    composable(route = "AccountInfo") {
                                        selectedRoute = "Konto"
                                        AccountInfo()
                                    }
                                }
                                //val data: Uri? = intent.data
                                //if (data != null && data.scheme == "boarderoo" && data.host == "paypal") {
                                //    navController.navigate("CartScreen")
                                //}
                            }
                        }
                    }
                }
            }
        }
    }
}