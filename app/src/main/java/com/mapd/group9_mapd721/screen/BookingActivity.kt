package com.mapd.group9_mapd721.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.mapd.group9_mapd721.ui.theme.Group9_MAPD721Theme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.unit.dp


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

val hotelImage =
    "https://cf.bstatic.com/xdata/images/hotel/max1280x900/157081615.jpg?k=e8b1951d8e07cd1dbf710065bfa7b2a1f3184ce31435ba6d59d1f6228eec9de9&o="

val bookingsData = listOf(
    BookingDemoData(
        bookingId = "BD001",
        imageUrl = hotelImage,
        hotelId = "H001",
        hotelName = "Luxury Hotel",
        bookedBy = "John Doe",
        checkInDate = "2024-05-01",
        checkOutDate = "2024-05-05",
        guests = 2,
        rooms = 1,
        totalCost = 500.00
    ),
    BookingDemoData(
        bookingId = "BD002",
        imageUrl = hotelImage,
        hotelId = "H002",
        hotelName = "Beach Resort",
        bookedBy = "Jane Smith",
        checkInDate = "2024-06-10",
        checkOutDate = "2024-06-15",
        guests = 4,
        rooms = 4,
        totalCost = 1200.00
    ),
    BookingDemoData(
        bookingId = "BD003",
        imageUrl = hotelImage,
        hotelId = "H003",
        hotelName = "Mountain Lodge",
        bookedBy = "Alice Johnson",
        checkInDate = "2024-07-20",
        checkOutDate = "2024-07-25",
        guests = 3,
        rooms = 3,
        totalCost = 800.00
    )
)

@Composable
fun BookingPage() {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(bookingsData.size) { index ->
                BookingCard(booking = bookingsData[index])
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
                text = "#" + booking.bookingId,
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
                    fontSize = 32.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Booked by",
                style = TextStyle(
                    fontWeight = FontWeight.W600,
                    fontSize = 16.sp
                )
            )

            Text(text = booking.bookedBy)

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
                        text = "Rooms",
                        style = TextStyle(
                            fontWeight = FontWeight.W600,
                            fontSize = 16.sp
                        )
                    )
                    Text(text = booking.rooms.toString())
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(0.5f)
                ) {
                    Text(
                        text = "Guests",
                        style = TextStyle(
                            fontWeight = FontWeight.W600,
                            fontSize = 16.sp
                        )
                    )
                    Text(text = booking.guests.toString())
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

//            Row(verticalAlignment = Alignment.Bottom) {
//                Text(
//                    text = "$${booking.totalCost}", style = TextStyle(
//                        fontWeight = FontWeight.W500,
//                        fontSize = 32.sp,
//                    )
//                )
//            }

            Column() {
                Text(
                    text = "Total Cost",
                    style = TextStyle(
                        fontWeight = FontWeight.W600,
                        fontSize = 16.sp
                    )
                )
                Text(text = "$${booking.totalCost}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingCardPreview() {
    Group9_MAPD721Theme {
        //HomePage()
        BookingCard(bookingsData[0])
    }
}