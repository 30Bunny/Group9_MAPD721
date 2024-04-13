package com.mapd.group9_mapd721.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bansidholakiya_mapd721_test.datastore.DataStoreManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mapd.group9_mapd721.MainActivity
import com.mapd.group9_mapd721.ui.theme.Group9_MAPD721Theme
import com.mapd.group9_mapd721.ui.theme.PrimaryColor
import com.mapd.group9_mapd721.ui.theme.TertiaryColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Group9_MAPD721Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val auth = FirebaseAuth.getInstance()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.padding(20.dp)
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Welcome Back",
                style = MaterialTheme.typography.titleLarge,
                color = PrimaryColor,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text("Login to access your account",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text("Email Address",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth().align(Alignment.Start).padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Email",
                    color = Color(0xFF888888).copy(alpha = 0.7f),) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(25.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = PrimaryColor,
                    unfocusedBorderColor = Color(0xFF888888))
            )

            Text("Password",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth().align(Alignment.Start).padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password",
                    color = Color(0xFF888888).copy(alpha = 0.7f),) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = PrimaryColor,
                    unfocusedBorderColor = Color(0xFF888888))
            )

            Spacer(modifier = Modifier.height(36.dp))

            Button(
                onClick = {
                    signInUser(
                        auth,
                        email,
                        password,
                        setLoading = { isLoading = it },
                        context,
                        snackbarHostState
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 16.dp
                ),
                modifier = Modifier.fillMaxWidth()
                    .height(56.dp) ,
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = PrimaryColor)
                } else {
                    Text("Sign In")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                Text("Don't have an account?")
                ClickableText(
                    text = buildAnnotatedString {
                        append(" Sign Up")
                        addStyle(
                            style = SpanStyle(
                                color = TertiaryColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            ), start = 0, end = length
                        )
                    },
                    onClick = {
                        // Navigate to RegisterActivity
                        context.startActivity(Intent(context, RegisterActivity::class.java))
                    }
                )
            }
        }
    }
}

fun signInUser(
    auth: FirebaseAuth,
    email: String,
    password: String,
    setLoading: (Boolean) -> Unit,
    context: Context,
    snackbarHostState: SnackbarHostState
) {

    if (email.isBlank() || password.isBlank()) {
        CoroutineScope(Dispatchers.IO).launch {
            showMessage(snackbarHostState, "Email or password cannot be empty")
        }
        return
    }

    setLoading(true)
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            setLoading(false)
            if (task.isSuccessful) {
                FirebaseFirestore.getInstance().collection("users").document(email)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            val name = document.getString("name") ?: ""
                            CoroutineScope(Dispatchers.IO).launch {
                                saveUserData(email = email, name = name, context)
                            }

                            // Sign in success, navigate to next screen or show a success message
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context.startActivity(intent)
                        } else {
                            CoroutineScope(Dispatchers.IO).launch {
                                showMessage(snackbarHostState, "Sign-in failed: No user found.")
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        exception.printStackTrace()
                        CoroutineScope(Dispatchers.IO).launch {
                            showMessage(snackbarHostState, "Sign-in failed: ${exception.message}")
                        }
                    }


            }
//            else {
//                // If sign in fails, display a message to the user.
//                CoroutineScope(Dispatchers.IO).launch {
//                    showMessage(snackbarHostState, "Sign-in failed. Please check your credentials.")
//                }
//            }
        }
        .addOnFailureListener { exception ->
            // Handle other exceptions
            CoroutineScope(Dispatchers.IO).launch {
                showMessage(snackbarHostState, "Sign-in failed: ${exception.message}")
            }
        }
}

suspend fun showMessage(snackbarHostState: SnackbarHostState, message: String?) {
    snackbarHostState.showSnackbar(
        message = message ?: "Login Failed, Please try again later."
    )
}

suspend fun saveUserData(email: String, name: String, context: Context) {
    val dataStore = DataStoreManager(context)
    dataStore.saveIsLogin(true);
    dataStore.saveUsername(email);
    dataStore.saveCName(name);
}

@Preview
@Composable
fun PreviewLoginScreen() {
    LoginScreen()
}