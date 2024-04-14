package com.mapd.group9_mapd721.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.mapd.group9_mapd721.R
import com.mapd.group9_mapd721.model.HotelDetails
import com.mapd.group9_mapd721.model.HotelListing
import com.mapd.group9_mapd721.ui.theme.Group9_MAPD721Theme
import com.mapd.group9_mapd721.ui.theme.PrimaryColor
import com.mapd.group9_mapd721.ui.theme.TertiaryColor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val imageList = listOf(
    "https://media.cntraveler.com/photos/5b2c0684a98055277ea83e26/1:1/w_2667,h_2667,c_limit/CN-Tower_GettyImages-615764386.jpg",
    "https://a.cdn-hotels.com/gdcs/production57/d1823/756d9f39-5aef-4974-a09b-4c8beed78e66.jpg",
    "https://i.natgeofe.com/n/77f528cb-054d-4bdb-8a4e-15a1c16d5195/winnipeg-skyline-canada_2x1.jpg",
)

private lateinit var hotelId: String

class HotelDetailActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hotelId = intent.getStringExtra("hotelId").toString()
        Log.d("hotelId", hotelId)

        setContent {
            Group9_MAPD721Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HotelDetailView()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HotelDetailView(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val activity = (LocalContext.current as? Activity)

    var isLoading by remember { mutableStateOf(true) }

    var hotel by remember {
        mutableStateOf<HotelDetails>(
            HotelDetails(
                "",
                "",
                "",
                "",
                "",
                0.0f,
                0.0,
                listOf(),
                "",
                listOf()
            )
        )
    }

    LaunchedEffect(Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            fetchHotelDetails(
                hotelId = hotelId,
                onSuccess = { item ->
                    hotel = item
                    isLoading = false
                    Log.d("HOTLE", hotel.toString())
                },
                onError = { error ->
                    Log.e("HomePage", "Error fetching data: $error")
                    isLoading = false
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Text("")
                }

            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
                tonalElevation = 8.dp
            ) {
                Row {
                    Column {
                        Text(
                            text = "Price",
                            style = MaterialTheme.typography.bodySmall,
                            color = TertiaryColor,
                            maxLines = 1
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$" + hotel.pricePerNight.toString(),
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                fontSize = 20.sp
                            )
                            Text(
                                text = "/ night",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                maxLines = 1
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    Button(
                        onClick = {
                            val intent = Intent(context, BookingDetailActivity::class.java).apply {
                                putExtra("hotel", hotel)
                            }
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 16.dp
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Book Now")
                    }
                }
            }

        },
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
            ) {
                ImageView(hotel)
                HotelNameHeader(hotel, modifier = modifier)
                HotelFacilityView(hotel, modifier = modifier)
                HotelDescriptionView(hotel, modifier = modifier)
                HotelImageList(hotel, modifier)
            }
        }
    }
}

@Composable
fun ImageView(hotel: HotelDetails) {
    Card(
        modifier = Modifier
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        val imageUrl = hotel.imageUrls.getOrNull(0)
        if (imageUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
//                        .height(400.dp)
                    .aspectRatio(1f / 1f)
            )
        } else {
            // You can show a placeholder or loading indicator here
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f / 1f) // You can adjust the aspect ratio as needed
                    .background(Color.LightGray), // Placeholder color
            ) {
                // You can customize the loading indicator here, for example, a progress bar
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun HotelNameHeader(hotel: HotelDetails, modifier: Modifier) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = hotel.hotelName,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                fontWeight = FontWeight.Medium,
                fontSize = 22.sp
            )
            RatingView(rating = hotel.rating, starIcon = Icons.Default.Star)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = hotel.city,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            fontSize = 18.sp,
            maxLines = 1
        )
    }
}

@Composable
fun RatingView(rating: Float, starIcon: ImageVector) {
    Row {
        Icon(
            imageVector = starIcon,
            contentDescription = null,
            tint = TertiaryColor,
            modifier = androidx.compose.ui.Modifier.size(24.dp)
        )
        Text(
            text = "$rating",
            color = Color.Black,
            modifier = androidx.compose.ui.Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun HotelDescriptionView(hotel: HotelDetails, modifier: Modifier) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Text(
            text = "Description",
            style = TextStyle(
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            ),
        )
        Spacer(modifier = modifier.height(8.dp))
        Text(
            text = hotel.description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            fontSize = 16.sp,
        )
    }

}

@Composable
fun HotelFacilityView(hotel: HotelDetails, modifier: Modifier) {
    val texts = hotel.facilities
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Text(
            text = "Facilities",
            style = TextStyle(
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            ),
        )
        Spacer(modifier = modifier.height(8.dp))
        GridOfRoundedOutlineContainers(texts = texts)
    }

}

@Composable
fun RoundedOutlineContainer(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .background(color = Color.Transparent, shape = RoundedCornerShape(8.dp))
            .border(
                BorderStroke(1.dp, Color.Gray.copy(alpha = 0.15f)),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TextStyle(textAlign = TextAlign.Center),
            color = TertiaryColor,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        )
    }
}

@Composable
fun GridOfRoundedOutlineContainers(
    texts: List<String>,
    columns: Int = 2,
) {
    val chunkedTexts = texts.chunked(columns)
    Column {
        chunkedTexts.forEach { rowTexts ->
            Row {
                rowTexts.forEach { text ->
                    RoundedOutlineContainer(text = text, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun HotelImageList(hotel: HotelDetails, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "Gallery",
            style = TextStyle(
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(
            //modifier = Modifier.padding(horizontal = 16.dp),
            content = {
                items(hotel.imageUrls.size) { index ->
                    HotelImageItem(
                        hotel.imageUrls[index],
                        isFirst = index == 0,
                        isLast = index == (10 - 1)
                    )
                }
            }
        )
        //StaggeredGridView(hotel)
    }
}

@Composable
fun HotelImageItem(item: String, isFirst: Boolean = false, isLast: Boolean = false) {
    val startPadding = if (isFirst) 16.dp else 6.dp
    val endPadding = if (isLast) 16.dp else 6.dp
    Card(
        modifier = Modifier
            .padding(start = startPadding, end = endPadding),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Image(
            painter = rememberAsyncImagePainter(item),
            //painter = painterResource(id = R.drawable.hotel_1_1),
            contentDescription = null,
            modifier = Modifier
                .height(80.dp)
                .width(80.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun fetchHotelDetails(
    hotelId: String,
    onSuccess: (HotelDetails) -> Unit,
    onError: (String) -> Unit
) {
    val client = OkHttpClient()

    val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val nextDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    val detailsRequest = Request.Builder()
        .url("https://booking-com.p.rapidapi.com/v2/hotels/details?currency=CAD&locale=en-us&checkout_date=$nextDate&hotel_id=$hotelId&checkin_date=$currentDate")
        .get()
        .addHeader("X-RapidAPI-Key", RAPID_API_KEY)
        .addHeader("X-RapidAPI-Host", RAPID_API_HOST)
        .build()

    val response = client.newCall(detailsRequest).execute()
    if (response.isSuccessful) {
        val responseData = response.body?.string()
        val hotel = parseHotelDetails(responseData)

        if (hotel.hotelName != "") {
            // Fetch description and images separately
            fetchDescriptionAndImages(hotelId, hotel, onSuccess, onError)
        } else {
            onError("Failed to parse hotel details")
        }
    } else {
        onError("Failed to fetch data")
    }
}

fun parseHotelDetails(responseData: String?): HotelDetails {
    var hotel = HotelDetails("", "", "", "", "", 0.0f, 0.0, listOf(), "", listOf())
    try {
        val jsonObject = JSONObject(responseData)
        val hotelId = jsonObject.optString("hotel_id")
        val hotelName = jsonObject.optString("hotel_name")
        val address = jsonObject.optString("address")
        val countryCode = jsonObject.optString("countrycode")
        val city = jsonObject.optString("city")
        val rating = jsonObject.optJSONObject("wifi_review_score")!!.optString("rating").toFloat()
        val decimalFormat = DecimalFormat("#.##")
        val pricePerNight = decimalFormat.format(
            jsonObject.optJSONObject("composite_price_breakdown")
                ?.optJSONObject("gross_amount_per_night")?.optDouble("value", 0.0)
        ).toDouble()

        val facilities = jsonObject.optJSONObject("facilities_block")?.optJSONArray("facilities")
            ?.let { jsonArrayToStringList(it) }

        hotel = hotel.copy(
            hotelId = hotelId,
            hotelName = hotelName,
            address = address,
            countryCode = countryCode,
            city = city,
            pricePerNight = pricePerNight ?: 0.0,
            rating = rating / 2,
            facilities = facilities ?: emptyList()
        )

        // Do whatever you want with the parsed data
        Log.d("hotel", "Hotel ID: $hotelId")
        Log.d("hotel", "Hotel Name: $hotelName")
        Log.d("hotel", "Address: $address")
        Log.d("hotel", "Country Code: $countryCode")
        Log.d("hotel", "City: $city")
        Log.d("hotel", "Price Per Night: $pricePerNight")
        Log.d("hotel", "Facilities: $facilities")

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return hotel
}

fun fetchDescriptionAndImages(
    hotelId: String,
    hotel: HotelDetails,
    onSuccess: (HotelDetails) -> Unit,
    onError: (String) -> Unit
) {
    val client = OkHttpClient()

    val descriptionRequest = Request.Builder()
        .url("https://booking-com.p.rapidapi.com/v1/hotels/description?hotel_id=$hotelId&locale=en-us")
        .get()
        .addHeader("X-RapidAPI-Key", RAPID_API_KEY)
        .addHeader("X-RapidAPI-Host", RAPID_API_HOST)
        .build()

    val imagesRequest = Request.Builder()
        .url("https://booking-com.p.rapidapi.com/v1/hotels/photos?hotel_id=$hotelId&locale=en-us")
        .get()
        .addHeader("X-RapidAPI-Key", RAPID_API_KEY)
        .addHeader("X-RapidAPI-Host", RAPID_API_HOST)
        .build()

    val descriptionResponse = client.newCall(descriptionRequest).execute()
    val imagesResponse = client.newCall(imagesRequest).execute()

    if (descriptionResponse.isSuccessful && imagesResponse.isSuccessful) {
        val descriptionData = descriptionResponse.body!!.string()
        val imagesData = imagesResponse.body!!.string()

        // Parse description and images data if required

        // Update hotel object with description and images
        val updatedHotel = hotel.copy(
            description = parseDescription(descriptionData),
            imageUrls = parseImages(imagesData)
        )

        onSuccess(updatedHotel)
    } else {
        onError("Failed to fetch description or images")
    }
}

fun parseDescription(descriptionData: String): String {
    var desc = ""
    try {
        val jsonObject = JSONObject(descriptionData)
        desc = jsonObject.optString("description")
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return desc
}

fun parseImages(imagesData: String?): List<String> {
    val imageUrls = mutableListOf<String>()

    // Check if imagesData is null or blank
    if (!imagesData.isNullOrBlank()) {
        try {
            // Parse the JSON array
            val jsonArray = JSONArray(imagesData)

            // Iterate through each object in the array
            for (i in 0 until jsonArray.length()) {
                // Break the loop if 10 URLs have been added
                if (imageUrls.size >= 10) {
                    break
                }

                // Get the current object
                val jsonObject = jsonArray.getJSONObject(i)

                // Extract the URL_1440 from the object
                val url1440 = jsonObject.optString("url_1440")

                // Add the URL to the list if it's not empty
                if (url1440.isNotEmpty()) {
                    imageUrls.add(url1440)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    return imageUrls
}

fun jsonArrayToStringList(jsonArray: JSONArray): List<String> {
    val list = mutableListOf<String>()
    for (i in 0 until jsonArray.length()) {
        val itemObject = jsonArray.optJSONObject(i)
        val name = itemObject?.optString("name")
        if (!name.isNullOrBlank()) {
            list.add(name)
        }
    }
    return list
}

@Composable
fun StaggeredGridView(hotel: HotelDetails) {
    // on below line we are creating
    // an array of images.
    val images = hotel.imageUrls

    Column {
        // on below line we are calling our
        // custom staggered vertical grid item.
        CustomStaggeredVerticalGrid(
            // on below line we are specifying
            // number of columns for our grid view.
            numColumns = 2,

            // on below line we are adding padding
            // from all sides for our grid view.
            modifier = Modifier.padding(5.dp)
        ) {
            // inside staggered grid view we are
            // adding images for each item of grid.
            images.forEach { img ->
                // on below line inside our grid
                // item we are adding card.
                Card(
                    // on below line inside the card we
                    // are adding modifier to it to specify
                    // max width, padding, elevation and shape for the card
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    // on below line we are adding column inside our card.
                    Column(
                        // in this column we are adding modifier
                        // to fill max size and align our
                        // card center horizontally.
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.CenterHorizontally),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // inside our column we are creating an image.
                        Image(
                            // on below line we are specifying the
                            // drawable image for our image.
//                            painterResource(id = img),
                            rememberAsyncImagePainter(img),


                            // on below line we are specifying
                            // content description for our image
                            contentDescription = "images",

                            // on below line we are specifying
                            // alignment for our image.
                            alignment = Alignment.Center,
                        )
                    }
                }
            }
        }
    }
}

// on below line we are creating a custom
// composable item for our grid view item.
@Composable
fun CustomStaggeredVerticalGrid(
    // on below line we are specifying
    // parameters as modifier, num of columns
    modifier: Modifier = Modifier,
    numColumns: Int = 2,
    content: @Composable () -> Unit
) {
    // inside this grid we are creating
    // a layout on below line.
    Layout(
        // on below line we are specifying
        // content for our layout.
        content = content,
        // on below line we are adding modifier.
        modifier = modifier
    ) { measurable, constraints ->
        // on below line we are creating a variable for our column width.
        val columnWidth = (constraints.maxWidth / numColumns)

        // on the below line we are creating and initializing our items constraint widget.
        val itemConstraints = constraints.copy(maxWidth = columnWidth)

        // on below line we are creating and initializing our column height
        val columnHeights = IntArray(numColumns) { 0 }

        // on below line we are creating and initializing placeables
        val placeables = measurable.map { measurable ->
            // inside placeable we are creating
            // variables as column and placeables.
            val column = testColumn(columnHeights)
            val placeable = measurable.measure(itemConstraints)

            // on below line we are increasing our column height/
            columnHeights[column] += placeable.height
            placeable
        }

        // on below line we are creating a variable for
        // our height and specifying height for it.
        val height =
            columnHeights.maxOrNull()?.coerceIn(constraints.minHeight, constraints.maxHeight)
                ?: constraints.minHeight

        // on below line we are specifying height and width for our layout.
        layout(
            width = constraints.maxWidth,
            height = height
        ) {
            // on below line we are creating a variable for column y pointer.
            val columnYPointers = IntArray(numColumns) { 0 }

            // on below line we are setting x and y for each placeable item
            placeables.forEach { placeable ->
                // on below line we are calling test
                // column method to get our column index
                val column = testColumn(columnYPointers)

                placeable.place(
                    x = columnWidth * column,
                    y = columnYPointers[column]
                )

                // on below line we are setting
                // column y pointer and incrementing it.
                columnYPointers[column] += placeable.height
            }
        }
    }
}

// on below line we are creating a test column method for setting height.
private fun testColumn(columnHeights: IntArray): Int {
    // on below line we are creating a variable for min height.
    var minHeight = Int.MAX_VALUE

    // on below line we are creating a variable for column index.
    var columnIndex = 0

    // on below line we are setting column  height for each index.
    columnHeights.forEachIndexed { index, height ->
        if (height < minHeight) {
            minHeight = height
            columnIndex = index
        }
    }
    // at last we are returning our column index.
    return columnIndex
}

@Preview(showBackground = true)
@Composable
fun HotelDetailPreview() {
    Group9_MAPD721Theme {
        //  HotelDetailView()
    }
}