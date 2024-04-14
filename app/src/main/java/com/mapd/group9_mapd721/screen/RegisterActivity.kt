package com.mapd.group9_mapd721.screen

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mapd.group9_mapd721.ui.theme.Group9_MAPD721Theme
import com.mapd.group9_mapd721.ui.theme.PrimaryColor
import com.mapd.group9_mapd721.ui.theme.TertiaryColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Group9_MAPD721Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegisterScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.padding(20.dp)
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            //verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Register",
                style = MaterialTheme.typography.titleLarge,
                color = PrimaryColor,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text("Fill the details to create an account",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text("Name",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth().align(Alignment.Start).padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Name",
                    color = Color(0xFF888888).copy(alpha = 0.7f),) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { /* Move focus to next field */ }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(25.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = PrimaryColor,
                    unfocusedBorderColor = Color(0xFF888888))
            )

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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { /* Move focus to next field */ }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        scope.launch {
                            registerUser(
                                context,
                                name,
                                email,
                                password,
                                setLoading = { isLoading = it },
                                snackbarHostState
                            )
                        }
                    }
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = PrimaryColor,
                    unfocusedBorderColor = Color(0xFF888888))
            )

            Spacer(modifier = Modifier.height(36.dp))

            Button(
                onClick = {
                    scope.launch {
                        registerUser(
                            context,
                            name,
                            email,
                            password,
                            setLoading = { isLoading = it },
                            snackbarHostState
                        )
                    }
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
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text("Register")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                Text("Already have an account?")
                ClickableText(
                    text = buildAnnotatedString {
                        append(" Sign In")
                        addStyle(
                            style = SpanStyle(
                                color = TertiaryColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            ), start = 0, end = length
                        )
                    },
                    onClick = {
                        (context as? Activity)?.finish()
                    }
                )
            }
        }
    }
}

suspend fun registerUser(
    context: Context,
    name: String,
    email: String,
    password: String,
    setLoading: (Boolean) -> Unit,
    snackbarHostState: SnackbarHostState
) {

    if (email.isBlank() || password.isBlank() || name.isBlank()) {
        CoroutineScope(Dispatchers.IO).launch {
            showMessage(snackbarHostState, "Email, name or password cannot be empty")
        }
        return
    }

    setLoading(true)
    try {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
        // Registration successful

        addUserToFirestore(name, email, setLoading, context, snackbarHostState)

    } catch (e: Exception) {
        // Registration failed
        Log.d("Firebase Registration Error", e.message.toString())
        setLoading(false)
        CoroutineScope(Dispatchers.IO).launch {
            showMessage(snackbarHostState, "Sign-up failed: ${e.message}")
        }
    }
}

fun addUserToFirestore(
    name: String,
    email: String,
    setLoading: (Boolean) -> Unit,
    context: Context,
    snackbarHostState: SnackbarHostState
) {
    val db = FirebaseFirestore.getInstance()
    val user = hashMapOf(
        "name" to name,
        "email" to email
    )

    db.collection("users").document(email)
        .set(user)
        .addOnSuccessListener {
            setLoading(false)
            // Navigate to next screen or show a success message
            CoroutineScope(Dispatchers.IO).launch {
                showMessage(snackbarHostState, "Registered successfully")
            }
            (context as? Activity)?.finish()

        }
        .addOnFailureListener { e ->
            setLoading(false)
            println("Error adding document: $e")
            CoroutineScope(Dispatchers.IO).launch {
                showMessage(snackbarHostState, "Sign-up failed: ${e.message}")
            }
        }
}


@Preview
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen()
}