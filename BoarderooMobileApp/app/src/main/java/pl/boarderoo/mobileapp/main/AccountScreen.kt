package pl.boarderoo.mobileapp.main

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pl.boarderoo.mobileapp.DialogBox
import pl.boarderoo.mobileapp.ElasticLightTextField
import pl.boarderoo.mobileapp.ErrorState
import pl.boarderoo.mobileapp.LightButton
import pl.boarderoo.mobileapp.R
import pl.boarderoo.mobileapp.datastore.AppRuntimeData
import pl.boarderoo.mobileapp.retrofit.services.UserService
import pl.boarderoo.mobileapp.retrofit.viewmodels.UserDetailsViewModel

@Composable
fun AccountInfo() {
    val viewModel: UserDetailsViewModel = viewModel()
    val user = viewModel.user.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()
    LaunchedEffect(user) {
        viewModel.getUserDetails(AppRuntimeData.user!!.email)
    }

    var changePassword by remember { mutableStateOf(false) }

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var repeatNewPassword by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val userService = UserService()

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (errorMessage.value != null) {
            ErrorState(
                errorMessage = errorMessage.value!!,
                onClose =  { viewModel.getUserDetails(AppRuntimeData.user!!.email) }
            )
        } else if (isLoading.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(200.dp),
                    color = colorResource(R.color.textColor),
                    trackColor = colorResource(R.color.buttonColor)
                )
            }
        } else {
            var name by remember { mutableStateOf(user.value!!.name) }
            var surname by remember { mutableStateOf(user.value!!.surname) }
            var address by remember { mutableStateOf(user.value!!.address) }
            Column(
                modifier = Modifier.padding(10.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text("Imię")
                    ElasticLightTextField(
                        placeholder = "",
                        value = name,
                        isError = false,
                        modifier = Modifier.width(300.dp)
                    ) {
                        name = it
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text("Nazwisko")
                    ElasticLightTextField(
                        placeholder = "",
                        value = surname,
                        isError = false,
                        modifier = Modifier.width(300.dp)
                    ) {
                        surname = it
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text("Adres")
                    ElasticLightTextField(
                        placeholder = "",
                        value = address,
                        isError = false,
                        modifier = Modifier.width(300.dp)
                    ) {
                        address = it
                    }
                }
                LightButton(
                    text = "Zapisz zmiany",
                    fontSize = 12.sp
                ) {
                    scope.launch {
                        val result = userService.editUser(
                            user.value!!.email,
                            if (name == "") null else name,
                            if (surname == "") null else surname,
                            if (address == "") null else address
                        )
                        if(result.isSuccessful) {
                            val toast = Toast.makeText(context, "Data saved", Toast.LENGTH_SHORT)
                            toast.show()
                            viewModel.getUserDetails(AppRuntimeData.user!!.email)
                        } else {
                            val toast = Toast.makeText(context, "Exception occurred", Toast.LENGTH_SHORT)
                            toast.show()
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                LightButton(
                    text = "Zmień hasło",
                    fontSize = 12.sp
                ) {
                    changePassword = true
                }
            }
            if(changePassword) {
                DialogBox(
                    closeDialog = { changePassword = false }
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ElasticLightTextField(
                            placeholder = "Stare hasło",
                            value = oldPassword,
                            isError = false,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            oldPassword = it
                        }
                        ElasticLightTextField(
                            placeholder = "Nowe hasło",
                            value = newPassword,
                            isError = false,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            newPassword = it
                        }
                        ElasticLightTextField(
                                placeholder = "Powtórz hasło",
                        value = repeatNewPassword,
                        isError = false,
                        modifier = Modifier.fillMaxWidth()
                        ) {
                            repeatNewPassword = it
                        }
                        Row {
                            LightButton(
                                text = "Potwierdź",
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1.0f)
                            ) {
                                scope.launch {
                                    val result = userService.updatePassword(
                                        user.value!!.email,
                                        oldPassword,
                                        newPassword
                                    )
                                    if(result.isSuccessful) {
                                        val toast = Toast.makeText(context, "Password updated", Toast.LENGTH_SHORT)
                                        toast.show()
                                        viewModel.getUserDetails(AppRuntimeData.user!!.email)
                                    } else {
                                        val toast = Toast.makeText(context, "Exception occurred", Toast.LENGTH_SHORT)
                                        toast.show()
                                    }
                                }
                            }
                            LightButton(
                                text = "Anuluj",
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1.0f)
                            ) {
                                changePassword = false
                                oldPassword = ""
                                newPassword = ""
                                repeatNewPassword = ""
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AccountInfoPreview() {
    AccountInfo()
}