package com.mapd.group9_mapd721.screen

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mapd.group9_mapd721.ui.theme.Group9_MAPD721Theme
import com.mapd.group9_mapd721.ui.theme.PrimaryColor

class PaymentActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Group9_MAPD721Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PaymentScreen()
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PaymentScreen() {
    var cardNumber by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var nameOnCard by remember { mutableStateOf("") }

    var cardNumberError by remember { mutableStateOf(false) }
    var expirationDateError by remember { mutableStateOf(false) }
    var cvvError by remember { mutableStateOf(false) }
    var nameOnCardError by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .padding(20.dp)
            .background(color = Color.White)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Card Number
            OutlinedTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                placeholder = {
                    Text(
                        "Card Number",
                        color = Color(0xFF888888).copy(alpha = 0.7f),
                    )
                },
                singleLine = true,
                isError = cardNumberError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { /* Move focus to next field */ }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .onFocusChanged { cardNumberError = it.isFocused && cardNumber.isEmpty() }
            )

            // Expiration Date
            OutlinedTextField(
                value = expirationDate,
                onValueChange = { expirationDate = it },
                placeholder = {
                    Text(
                        "Expiration Date (MM/YYYY)",
                        color = Color(0xFF888888).copy(alpha = 0.7f),
                    )
                },
                singleLine = true,
                isError = expirationDateError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { /* Move focus to next field */ }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .onFocusChanged {
                        expirationDateError = it.isFocused && expirationDate.isEmpty()
                    }
            )

            // CVV
            OutlinedTextField(
                value = cvv,
                onValueChange = { cvv = it },
                placeholder = {
                    Text(
                        "CVV",
                        color = Color(0xFF888888).copy(alpha = 0.7f),
                    )
                },
                singleLine = true,
                isError = cvvError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { /* Move focus to next field */ }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
                    .onFocusChanged { cvvError = it.isFocused && cvv.isEmpty() },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = PrimaryColor,
                    unfocusedBorderColor = Color(0xFF888888)
                )
            )

            // Name on Card
            OutlinedTextField(
                value = nameOnCard,
                onValueChange = { nameOnCard = it },
                placeholder = {
                    Text(
                        "Name on Card",
                        color = Color(0xFF888888).copy(alpha = 0.7f),
                    )
                },
                singleLine = true,
                isError = nameOnCardError,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { /* Handle Done action if needed */ }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .onFocusChanged { nameOnCardError = it.isFocused && nameOnCard.isEmpty() }
            )

            Button(
                onClick = {
                    // Perform validation
                    cardNumberError = cardNumber.isEmpty()
                    expirationDateError = expirationDate.isEmpty()
                    cvvError = cvv.isEmpty()
                    nameOnCardError = nameOnCard.isEmpty()

                    // If all fields are filled, proceed with payment
                    if (!cardNumberError && !expirationDateError && !cvvError && !nameOnCardError) {
                        // Perform payment
                        performPayment()
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Pay")
            }
        }
    }
}

fun performPayment() {
    // Perform payment logic here
}

@Preview
@Composable
fun PaymentScreenPreview() {
    PaymentScreen()
}