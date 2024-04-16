package com.mapd.group9_mapd721.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.mapd.group9_mapd721.ui.theme.Group9_MAPD721Theme
import com.example.bansidholakiya_mapd721_test.datastore.DataStoreManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.launch
import java.text.DecimalFormat

data class BookingDemoData(
    val bookingId: String,
    val imageUrl: String,
    val hotelId: String,
    val hotelName: String,
    val bookedBy: String,
    val checkInDate: String,
    val checkOutDate: String,
    val guests: Int,
    val rooms: Int,
    val totalCost: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingPage() {
    val context = LocalContext.current
    val dataStore = DataStoreManager(context)
    val coroutineScope = rememberCoroutineScope()
    val bookingsData = remember { mutableStateOf(listOf<BookingDemoData>()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val userID = dataStore.readUsername()
            val userName = dataStore.readCName()
            getBookings(userID, userName) { fetchedBookings ->
                bookingsData.value = fetchedBookings
            }
        }
    }

    Scaffold { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally) {
            if (bookingsData.value.isEmpty()) {
                // Display a message when there are no bookings
                Text(
                    text = "No bookings available",
                    modifier = Modifier.padding(16.dp)
                        .fillMaxHeight(),
                    style = TextStyle(
                        fontWeight = FontWeight.W500,
                        fontSize = 20.sp,
                    )
                )
            } else {
                LazyColumn {
                    items(bookingsData.value) { booking ->
                        BookingCard(booking = booking)
                    }
                }
            }
        }
    }
}


@Composable
fun BookingCard(booking: BookingDemoData) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "#HM-" + booking.bookingId,
                style = TextStyle(
                    fontWeight = FontWeight.W500,
                    fontSize = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Image(
                painter = rememberAsyncImagePainter(booking.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.FillBounds
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = booking.hotelName, style = TextStyle(
                    fontWeight = FontWeight.W500,
                    fontSize = 24.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(0.5f)
                ) {
                    Text(
                        text = "Booked by",
                        style = TextStyle(
                            fontWeight = FontWeight.W600,
                            fontSize = 16.sp
                        )
                    )
                    Text(text = booking.bookedBy)
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(0.5f)
                ) {
                    Text(
                        text = "Rooms",
                        style = TextStyle(
                            fontWeight = FontWeight.W600,
                            fontSize = 16.sp
                        )
                    )
                    Text(text = booking.rooms.toString())
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(0.5f)
                ) {
                    Text(
                        text = "Check-in",
                        style = TextStyle(
                            fontWeight = FontWeight.W600,
                            fontSize = 16.sp
                        )
                    )
                    Text(text = booking.checkInDate)
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(0.5f)
                ) {
                    Text(
                        text = "Check-out",
                        style = TextStyle(
                            fontWeight = FontWeight.W600,
                            fontSize = 16.sp
                        )
                    )
                    Text(text = booking.checkOutDate)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column() {
                Text(
                    text = "Total Cost",
                    style = TextStyle(
                        fontWeight = FontWeight.W600,
                        fontSize = 16.sp
                    )
                )
                val formattedTotalCost = DecimalFormat("0.00").format(booking.totalCost)
                Text(text = "$$formattedTotalCost")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingCardPreview() {
    Group9_MAPD721Theme {
        BookingCard(booking = BookingDemoData(
            bookingId = "BD001",
            imageUrl = "https://cf.bstatic.com/xdata/images/hotel/max1280x900/157081615.jpg?k=e8b1951d8e07cd1dbf710065bfa7b2a1f3184ce31435ba6d59d1f6228eec9de9&o=",
            hotelId = "H001",
            hotelName = "Luxury Hotel",
            bookedBy = "John Doe",
            checkInDate = "2024-05-01",
            checkOutDate = "2024-05-05",
            guests = 2,
            rooms = 1,
            totalCost = 500.00
        ))
    }
}

fun getBookings(
    userID: String,
    userName: String,
    onBookingsFetched: (List<BookingDemoData>) -> Unit
) {
    FirebaseFirestore.getInstance().collection("users")
        .document(userID)
        .collection("bookings")
        .get()
        .addOnSuccessListener { querySnapshot: QuerySnapshot ->
            val bookings = querySnapshot.documents.map { document ->
                val data = document.data
                val numberOfRooms = data?.get("numberOfRooms")
                val rooms = when (numberOfRooms) {
                    is Number -> numberOfRooms.toInt() // Convert Number to Int
                    else -> {
                        Log.e("BookingData", "Unexpected type for numberOfRooms: ${numberOfRooms?.javaClass?.simpleName}")
                        0 // Default value in case of unexpected type or null
                    }
                }
                BookingDemoData(
                    bookingId =  document.id.takeLast(6),
                    imageUrl = data?.get("hotelImage") as String,
                    hotelId = "1",
                    hotelName = data["hotelName"] as String,
                    bookedBy = userName,
                    checkInDate = data["checkInDate"] as String,
                    checkOutDate = data["checkOutDate"] as String,
                    guests = 2,
                    rooms = rooms,
                    totalCost = data["totalAmount"] as Double )
            }
            onBookingsFetched(bookings)
        }
        .addOnFailureListener { e ->
            Log.w("Booking_Failed", "Error fetching bookings", e)
        }
}
