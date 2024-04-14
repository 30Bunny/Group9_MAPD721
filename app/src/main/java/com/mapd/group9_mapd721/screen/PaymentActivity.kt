package com.mapd.group9_mapd721.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bansidholakiya_mapd721_test.datastore.DataStoreManager
import com.google.firebase.firestore.FirebaseFirestore
import com.mapd.group9_mapd721.MainActivity
import com.mapd.group9_mapd721.R
import com.mapd.group9_mapd721.model.Booking
import com.mapd.group9_mapd721.model.HotelDetails
import com.mapd.group9_mapd721.ui.theme.Group9_MAPD721Theme
import com.mapd.group9_mapd721.ui.theme.PrimaryColor
import com.mapd.group9_mapd721.ui.theme.TertiaryColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.UUID

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

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val dataStore = DataStoreManager(context)

    val activity = (LocalContext.current as? Activity)
    val totalPrice = activity?.intent?.getDoubleExtra("total_price", 0.0)
    val rooms = activity?.intent?.getIntExtra("number_of_rooms", 0)
    val checkInDate = activity?.intent?.getStringExtra("check_in_date")
    val checkOutDate = activity?.intent?.getStringExtra("check_out_date")
    val hotel = activity?.intent?.getSerializableExtra("hotel") as? HotelDetails

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier
            //.padding(20.dp)
            .background(color = Color.White),
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                tonalElevation = 8.dp
            ) {
                Row{
                    Column {
                        Text(
                            text = "Total Price",
                            style = MaterialTheme.typography.bodySmall,
                            color = TertiaryColor,
                            maxLines = 1
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$${String.format("%.2f", totalPrice)}",
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                fontSize = 20.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    Button(
                        onClick = {
                            // Perform validation
                            cardNumberError = cardNumber.isEmpty()
                            expirationDateError = expirationDate.isEmpty()
                            cvvError = cvv.isEmpty()
                            nameOnCardError = nameOnCard.isEmpty()

                            CoroutineScope(Dispatchers.IO).launch {
                                // Perform payment
                                if (cardNumberError) {
                                    showMessage(snackbarHostState, "Please enter card number")
                                } else if (cardNumber.length != 16) {
                                    showMessage(snackbarHostState, "Please enter valid card number")
                                } else if (expirationDateError) {
                                    showMessage(snackbarHostState, "Please enter expiry date")
                                } else if (!isValidExpirationDate(expirationDate)) {
                                    showMessage(snackbarHostState, "Please enter valid expiry date")
                                } else if (cvvError) {
                                    showMessage(snackbarHostState, "Please enter cvv")
                                } else if (cvv.length != 3) {
                                    showMessage(snackbarHostState, "Please enter valid cvv")
                                } else if (nameOnCardError) {
                                    showMessage(snackbarHostState, "Please enter name")
                                } else {

                                    val booking = Booking(
                                        checkInDate ?: "",
                                        checkOutDate ?: "",
                                        rooms ?: 0,
                                        hotel?.hotelName ?: "",
                                        hotel?.city ?: "",
                                        hotel?.imageUrls?.get(0) ?: "",
                                        totalPrice ?: 0.0
                                    )

                                    val userID = dataStore.readUsername()

                                    performPayment(booking, userID, context)
                                }
                            }

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 16.dp
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Pay Now")
                    }
                }
            }

        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.credit_card),
                contentDescription = null,
            )

            Spacer(Modifier.height(24.dp))

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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { /* Move focus to next field */ }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = PrimaryColor,
                    unfocusedBorderColor = Color(0xFF888888)
                )
            )

            // Expiration Date
            OutlinedTextField(
                value = TextFieldValue(
                    text = expirationDate,
                    selection = TextRange(expirationDate.length)
                ),
                onValueChange = {
                    if (it.text.length == 2 && it.text.last() != '/') {
                        expirationDate = "${it.text}/"
                    } else {
                        expirationDate = it.text
                    }
                },
                placeholder = {
                    Text(
                        "Expiration Date (MM/YYYY)",
                        color = Color(0xFF888888).copy(alpha = 0.7f),
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { /* Move focus to next field */ }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = PrimaryColor,
                    unfocusedBorderColor = Color(0xFF888888)
                )
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { /* Move focus to next field */ }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
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
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { /* Handle Done action if needed */ }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = PrimaryColor,
                    unfocusedBorderColor = Color(0xFF888888)
                )
            )
        }
    }
}

fun performPayment(booking: Booking, userID: String, context: Context) {
    // Generate a unique booking ID
    val bookingId = UUID.randomUUID().toString()

    // Create a map to store booking information
    val bookingMap: HashMap<String, Any> = hashMapOf(
        "checkInDate" to booking.checkInDate,
        "checkOutDate" to booking.checkOutDate,
        "numberOfRooms" to booking.numberOfRooms,
        "hotelName" to booking.hotelName,
        "hotelCity" to booking.hotelCity,
        "hotelImage" to booking.hotelImage,
        "totalAmount" to booking.totalAmount
    )

    FirebaseFirestore.getInstance().collection("users")
        .document(userID)
        .collection("bookings")
        .document(bookingId)
        .set(bookingMap)
        .addOnSuccessListener {
            Log.d("Booking_Added", "Booking added successfully!")
            val intent = Intent(context, BookingSuccessActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
        .addOnFailureListener { e ->
            Log.w("Booking_Failed", "Error adding booking", e)
        }
}

@SuppressLint("NewApi")
private fun isValidExpirationDate(expirationDate: String): Boolean {
    // Check if the expiration date is in MM/YYYY format
    if (!expirationDate.matches(Regex("""^\d{2}/\d{4}$"""))) {
        return false
    }

    // Split the expiration date into month and year
    val parts = expirationDate.split("/")
    val month = parts[0].toIntOrNull() ?: return false
    val year = parts[1].toIntOrNull() ?: return false

    // Check if the expiration date is in the future
    val currentYear = YearMonth.now().year
    val currentMonth = YearMonth.now().monthValue
    if (year < currentYear || (year == currentYear && month < currentMonth)) {
        return false
    }

    return true
}

@Preview
@Composable
fun PaymentScreenPreview() {
    PaymentScreen()
}