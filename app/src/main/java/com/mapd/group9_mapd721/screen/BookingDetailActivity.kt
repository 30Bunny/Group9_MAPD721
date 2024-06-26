@file:OptIn(ExperimentalMaterial3Api::class)

package com.mapd.group9_mapd721.screen

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mapd.group9_mapd721.ui.theme.Group9_MAPD721Theme
import com.mapd.group9_mapd721.ui.theme.PrimaryColor
import com.mapd.group9_mapd721.ui.theme.TertiaryColor
import kotlinx.coroutines.launch
import java.time.Instant
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class BookingDetailActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Group9_MAPD721Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GuestListScreen()
                }
            }
        }
    }
}

data class Guest(val name: String, val age: Int, val gender: String)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GuestListScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val activity = (LocalContext.current as? Activity)
    val totalPrice = remember { mutableStateOf(0.0) }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Text("Majestic Plaza Hotel")
                }

            )
        },
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
                                text = "$${totalPrice.value}",
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                fontSize = 20.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    Button(
                        onClick = {
                            context.startActivity(Intent(context, PaymentActivity::class.java))
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
    ) {
            innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(scrollState)) {
            BookingDetailsNew(modifier = modifier, totalPrice = totalPrice)
        }
    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingDetailsNew(modifier: Modifier = Modifier, totalPrice: MutableState<Double>) {
    var showDialog by remember { mutableStateOf(false) }
    val guestList = remember { mutableStateListOf<Guest>() }
    var rooms by rememberSaveable { mutableStateOf(1f) } // Default to 1 room
    val checkInDate = rememberSaveable { mutableStateOf(LocalDate.now())}
    val checkOutDate = rememberSaveable { mutableStateOf(LocalDate.now().plusDays(1))} // Default to next day

    // Calculate the number of nights
    val nights = checkOutDate.value.toEpochDay() - checkInDate.value.toEpochDay()

    // Calculate the total price
    val calculatedTotalPrice = calculateTotalPrice(rooms.toInt(), nights)
    // Update the shared total price state
    totalPrice.value = calculatedTotalPrice

    Column(modifier = Modifier.padding(16.dp)) {
        CustomDatePicker(checkInDate, checkOutDate)
        Spacer(modifier = Modifier.height(16.dp))
        if (showDialog) {
            AddGuestDialog(
                showDialog = showDialog,
                onDismiss = { showDialog = false },
                onAddGuest = { guest ->
                    guestList.add(guest)
                    showDialog = false
                }
            )
        }

        // Slider for selecting the number of rooms
        Text("Number of Rooms: ${rooms.toInt()}")
        Slider(
            value = rooms,
            onValueChange = { newValue -> rooms = newValue },
            valueRange = 1f..10f, // Assuming the user can select from 1 to 10 rooms
            steps = 10, // Step size, adjust according to your needs
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { showDialog = true }) {
            Text("Add Guest")
        }

        Spacer(modifier = Modifier.height(16.dp))
        GuestList(guestList = guestList)

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    onAccept: (Long?) -> Unit,
    onCancel: () -> Unit
) {
    val state = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = { },
        confirmButton = {
            Button(onClick = { onAccept(state.selectedDateMillis) }) {
                Text("Accept")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = state)
    }
}



@Composable
fun AddGuestDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onAddGuest: (Guest) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text("Add Guest") },
            text = {
                Column {
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") }
                    )
                    TextField(
                        value = age.toString(),
                        onValueChange = { age = it },
                        label = { Text("Age") }
                    )
                    TextField(
                        value = gender,
                        onValueChange = { gender = it },
                        label = { Text("Gender") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (name.isNotBlank() && age.isNotBlank() && gender.isNotBlank()) {
                        onAddGuest(Guest(name, age.toInt(), gender))
                    }
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun GuestList(guestList: MutableList<Guest>) {
    val coroutineScope = rememberCoroutineScope()
    Column {
        guestList.forEach { guest ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${guest.name}, ${guest.age}, ${guest.gender}")
                IconButton(onClick = {
                    coroutineScope.launch {
                        guestList.remove(guest)
                    }
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Guest")
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomDatePicker(checkInDate: MutableState<LocalDate>, checkOutDate: MutableState<LocalDate>) {
    val isCheckInOpen = remember { mutableStateOf(false)}
    val isCheckOutOpen = remember { mutableStateOf(false)}

    Column(modifier = Modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                readOnly = true,
                value = checkInDate.value.format(DateTimeFormatter.ISO_DATE),
                label = { Text("Check-In Date") },
                onValueChange = {})
            IconButton(
                onClick = { isCheckInOpen.value = true } // show de dialog
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Calendar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                readOnly = true,
                value = checkOutDate.value.format(DateTimeFormatter.ISO_DATE),
                label = { Text("Check-Out Date") },
                onValueChange = {})
            IconButton(
                onClick = { isCheckOutOpen.value = true } // show de dialog
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Calendar")
            }
        }
    }

    if (isCheckInOpen.value) {
        CustomDatePickerDialog(
            onAccept = {
                isCheckInOpen.value = false // close dialog
                if (it != null) { // Set the check-in date
                    checkInDate.value = Instant
                        .ofEpochMilli(it)
                        .atZone(ZoneId.of("UTC"))
                        .toLocalDate()

                }
            },
            onCancel = {
                isCheckInOpen.value = false //close dialog
            }
        )
    }

    if (isCheckOutOpen.value) {
        CustomDatePickerDialog(
            onAccept = {
                isCheckOutOpen.value = false // close dialog
                if (it != null) { // Set the check-out date
                    checkOutDate.value = Instant
                        .ofEpochMilli(it)
                        .atZone(ZoneId.of("UTC"))
                        .toLocalDate()
                }
            },
            onCancel = {
                isCheckOutOpen.value = false //close dialog
            }
        )
    }
}

fun calculateTotalPrice(rooms: Int, nights: Long): Double {
    var totalPrice = 0.0
    for (i in 0 until rooms) {
        val basePrice = if (i == 0) 100.0 else 75.0
        totalPrice += basePrice * nights
    }
    return totalPrice
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GuestListPreview() {
    Group9_MAPD721Theme {
        GuestListScreen()
    }
}


