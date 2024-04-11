package com.mapd.group9_mapd721.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.mapd.group9_mapd721.R
import com.mapd.group9_mapd721.ui.theme.Group9_MAPD721Theme
import com.mapd.group9_mapd721.ui.theme.PrimaryColor
import com.mapd.group9_mapd721.ui.theme.TertiaryColor

val imageList = listOf(
    "https://media.cntraveler.com/photos/5b2c0684a98055277ea83e26/1:1/w_2667,h_2667,c_limit/CN-Tower_GettyImages-615764386.jpg",
    "https://a.cdn-hotels.com/gdcs/production57/d1823/756d9f39-5aef-4974-a09b-4c8beed78e66.jpg",
    "https://i.natgeofe.com/n/77f528cb-054d-4bdb-8a4e-15a1c16d5195/winnipeg-skyline-canada_2x1.jpg",
)

class HotelDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HotelDetailView(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    val activity = (LocalContext.current as? Activity)
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
        }
    ) {
            innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(scrollState)) {
            ImageView()
            HotelNameHeader(modifier = modifier)
            HotelFacilityView(modifier = modifier)
            HotelDescriptionView(modifier = modifier)
            HotelImageList(modifier)
        }
    }
}

@Composable
fun ImageView() {
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
        Image(
            painter = painterResource(id = R.drawable.hotel_1_1), // Replace with your image resource
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun HotelNameHeader(modifier: Modifier) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Majestic Palza Hotel",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                fontWeight = FontWeight.Medium,
                fontSize = 22.sp
            )
            RatingView(rating = 4.9f, starIcon = Icons.Default.Star)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "London, UK",
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
fun HotelDescriptionView(modifier: Modifier) {
    val texts = listOf("Free Wi-Fi", "Air conditioning", "Breakfast included", "Restaurant", "City View", "Swimming pool")
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
            text = "Upscale hotel with restaurant, near Bell Centre. A hotel is an establishment that provides lodging, meals, and various guest services for travelers and tourists.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            fontSize = 16.sp,
        )
    }

}

@Composable
fun HotelFacilityView(modifier: Modifier) {
    val texts = listOf("Free Wi-Fi", "Air conditioning", "Breakfast included", "Restaurant", "Laundry", "Swimming pool")
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
        Text(text = text, color = TertiaryColor, fontWeight = FontWeight.Normal, fontSize = 14.sp)
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
fun HotelImageList(modifier: Modifier = Modifier) {
    Column{
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
//        LazyRow(
//            //modifier = Modifier.padding(horizontal = 16.dp),
//            content = {
//                items(imageList.size) { index ->
//                    HotelImageItem(imageList[index], isFirst = index == 0, isLast = index == (10 - 1))
//                }
//            }
//        )
        StaggeredGridView()
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

@Composable
fun StaggeredGridView() {
    // on below line we are creating
    // an array of images.
    val images = listOf(
        R.drawable.hotel_1_1,
        R.drawable.hotel_1_2,
        R.drawable.hotel_1_4,
        R.drawable.hotel_1_3,
        R.drawable.hotel_1_5,
    )

    Column{
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
                            painterResource(id = img),

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
        HotelDetailView()
    }
}