package com.mapd.group9_mapd721.screen

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.mapd.group9_mapd721.R
import com.mapd.group9_mapd721.model.HotelDetailRoute
import com.mapd.group9_mapd721.model.HotelListing
import com.mapd.group9_mapd721.ui.theme.BG
import com.mapd.group9_mapd721.ui.theme.Group9_MAPD721Theme
import com.mapd.group9_mapd721.ui.theme.PrimaryColor
import com.example.bansidholakiya_mapd721_test.datastore.DataStoreManager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Step 1: Data class representing the items in the list
data class PlaceListItem(val imageUrl: String, val name: String)

// Step 2: Sample list of items
val itemList = listOf(
    PlaceListItem(
        "https://media.cntraveler.com/photos/5b2c0684a98055277ea83e26/1:1/w_2667,h_2667,c_limit/CN-Tower_GettyImages-615764386.jpg",
        "Toronto"
    ),
    PlaceListItem(
        "https://a.cdn-hotels.com/gdcs/production57/d1823/756d9f39-5aef-4974-a09b-4c8beed78e66.jpg",
        "Montreal"
    ),
    PlaceListItem(
        "https://i.natgeofe.com/n/77f528cb-054d-4bdb-8a4e-15a1c16d5195/winnipeg-skyline-canada_2x1.jpg",
        "Winnipeg"
    ),
    PlaceListItem(
        "https://upload.wikimedia.org/wikipedia/commons/2/22/Parliament-Ottawa.jpg",
        "Ottawa"
    ),
    PlaceListItem(
        "https://upload.wikimedia.org/wikipedia/commons/5/57/Concord_Pacific_Master_Plan_Area.jpg",
        "Vancouver"
    ),
)


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePage(navController: NavController) {
    val context = LocalContext.current
    val dataStore = DataStoreManager(context)
    val scrollState = rememberScrollState()


    val cName = remember { mutableStateOf("") }
    var hotelList by remember { mutableStateOf<List<HotelListing>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) } // Loading state

    // Fetch hotels when the screen is initially displayed
    LaunchedEffect(Unit) {
        cName.value = dataStore.readCName()

        fetchHotels(
            onSuccess = { hotels ->
                hotelList = hotels
                isLoading = false // Set loading state to false when data is fetched
                Log.d("HomePage", "Data fetched successfully")
            },
            onError = { error ->
                // Handle error
                isLoading = false // Set loading state to false in case of error
                Log.e("HomePage", "Error fetching data: $error")
            }
        )
    }

    Log.d("HomePage", "isLoading: $isLoading")

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = PrimaryColor)
        .verticalScroll(scrollState)) {
        TopContainer(cName.value)
        if (isLoading) {
            // Show progress indicator if loading
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
                Log.d("HomePage", "Loading indicator shown")
            }
        } else {
            // Show hotel list if not loading
            HotelList(hotelList) { hotelId ->
                navigateToHotelDetailScreen(context, hotelId)
            }
            Log.d("HomePage", "Hotel list shown")
        }
        PlaceList()
        Box(
            modifier = Modifier
                .height(40.dp)
                .background(color = Color.White)
        )
    }
}

@Composable
fun TopContainer(cName: String) {
    var searchText by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(168.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Text(
                text = "Good Morning, $cName!",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 24.sp,
                ),
                modifier = Modifier.padding(top = 16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            CircularSearchBar(
                modifier = Modifier.fillMaxWidth(),
                onSearchTextChanged = { searchText = it },
                searchText = searchText
            )
        }
    }
}

@Composable
fun CircularSearchBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White.copy(alpha = 0.5f),
    hint: String = "Search...",
    onSearchTextChanged: (String) -> Unit,
    searchText: String
) {
    Surface(
        modifier = modifier.padding(horizontal = 0.dp),
        shape = CircleShape,
        color = backgroundColor
    ) {
        Row(
            modifier = modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchText,
                onValueChange = { onSearchTextChanged(it) },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                placeholder = { Text(text = hint, color = Color.White) },
                singleLine = true,
                textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }

    }
}

@Composable
fun HotelList(hotelList: List<HotelListing>,modifier: Modifier = Modifier, onItemClick: (String) -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(color = Color.White)
            .padding(vertical = 8.dp),
    ) {
        Column() {
            Text(
                text = "Hotel Near You",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp,
                ),
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                //modifier = Modifier.padding(horizontal = 16.dp),
                content = {
                    items(hotelList.size) { index ->
                        HotelItem(hotelList[index], isFirst = index == 0, isLast = index == hotelList.size - 1, onItemClick)
                    }
                }
            )
        }
    }
}

@Composable
fun HotelItem(hotel: HotelListing, isFirst: Boolean, isLast: Boolean, onItemClick: (String) -> Unit) {
    val startPadding = if (isFirst) 16.dp else 8.dp
    val endPadding = if (isLast) 16.dp else 8.dp
    Card(
        modifier = Modifier
            .padding(start = startPadding, end = endPadding).clickable { onItemClick(hotel.id) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .width(240.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(hotel.hotelImage), // Replace with your image resource
                contentDescription = null,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp)
            ) {
                Text(
                    text = hotel.hotelName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = hotel.city,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = hotel.price.toString(),
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "/ night",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun PlaceList(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(top = 8.dp, bottom = 60.dp),
    ) {
        Column() {
            Text(
                text = "Explore Places",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp,
                ),
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                //modifier = Modifier.padding(horizontal = 16.dp),
                content = {
                    items(itemList.size) { index ->
                        PlaceItem(itemList[index], isFirst = index == 0, isLast = index == (10 - 1))
                    }
                }
            )
        }
    }
}

@Composable
fun PlaceItem(item: PlaceListItem, isFirst: Boolean = false, isLast: Boolean = false) {
    val startPadding = if (isFirst) 16.dp else 8.dp
    val endPadding = if (isLast) 16.dp else 8.dp
    Card(
        modifier = Modifier
            .padding(start = startPadding, end = endPadding),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = rememberAsyncImagePainter(item.imageUrl),
                //painter = painterResource(id = R.drawable.hotel_1_1),
                contentDescription = null,
                modifier = Modifier
                    .height(146.dp)
                    .width(146.dp)
                    .fillMaxWidth()
                    .drawWithCache {
                        val gradient = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f)),
                            startY = size.height,
                            endY = size.height
                        )
                        onDrawWithContent {
                            drawContent()
                            drawRect(gradient, blendMode = BlendMode.Multiply)
                        }
                    },
                contentScale = ContentScale.Crop
            )
            Text(item.name, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 18.sp)
        }
    }
}

fun navigateToHotelDetailScreen(context: Context, hotelId: String) {
    val intent = Intent(context, HotelDetailActivity::class.java).apply {
        putExtra("hotelId", hotelId)
    }
    context.startActivity(intent)
}


@RequiresApi(Build.VERSION_CODES.O)
fun fetchHotels(onSuccess: (List<HotelListing>) -> Unit, onError: (String) -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
        val client = OkHttpClient()

        // Calculate current date and next date
        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val nextDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        val request = Request.Builder()
            .url("https://booking-com.p.rapidapi.com/v1/hotels/search?checkout_date=$nextDate&order_by=popularity&filter_by_currency=CAD&room_number=1&dest_id=38&dest_type=country&adults_number=2&checkin_date=$currentDate&locale=en-us&units=metric")
            .get()
            .addHeader("X-RapidAPI-Key", "1a071bbc5fmsh274b3c6c1ae9fffp1fdde7jsn5c083c6a4219")
            .addHeader("X-RapidAPI-Host", "booking-com.p.rapidapi.com")
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val responseData = response.body?.string()
            val hotels = parseHotels(responseData)
            onSuccess(hotels)
        } else {
            onError("Failed to fetch data")
        }
    }
}

fun parseHotels(responseData: String?): List<HotelListing> {
    val hotels = mutableListOf<HotelListing>()
    responseData?.let {
        val jsonObject = JSONObject(it)
        val hotelsArray = jsonObject.getJSONArray("result")
        for (i in 0 until hotelsArray.length()) {
            val hotelObject = hotelsArray.getJSONObject(i)
            val hotelName = hotelObject.optString("hotel_name", "")
            val hotelImage = hotelObject.optString("max_photo_url", "")
            val hotelId = hotelObject.optString("hotel_id", "")
            val decimalFormat = DecimalFormat("#.##")
            val price = decimalFormat.format(hotelObject.optDouble("min_total_price", 0.0)).toDouble()
            val city = hotelObject.optString("city", "")

            val hotelListing = HotelListing(hotelId, hotelName, hotelImage , price, city)
            //Log.d("List", hotelListing.toString())
            hotels.add(hotelListing)
        }
    }
    Log.d("List", hotels.toString())
    return hotels
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Group9_MAPD721Theme {
        //HomePage()
    }
}