package com.mapd.group9_mapd721.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.shapes.Shape
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.mapd.group9_mapd721.R
import com.mapd.group9_mapd721.model.HotelDetailRoute
import com.mapd.group9_mapd721.ui.theme.BG
import com.mapd.group9_mapd721.ui.theme.Group9_MAPD721Theme
import com.mapd.group9_mapd721.ui.theme.PrimaryColor

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

@Composable
fun HomePage(navController: NavController) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = PrimaryColor)
        .verticalScroll(scrollState)) {
        TopContainer()
        HotelList{
            //navigate to Hotel Detail page using NavController
            //navController.navigate(HotelDetailRoute)

            //navigate to Hotel Detail page using local context and startActivity
            context.startActivity(Intent(context, HotelDetailActivity::class.java))
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
fun TopContainer() {
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
                text = "Good Morning, Bansi !",
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
fun HotelList(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .clickable(onClick = onClick)
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
                    items(10) { index ->
                        HotelItem(index, isFirst = index == 0, isLast = index == (10 - 1))
                    }
                }
            )
        }
    }
}

@Composable
fun HotelItem(index: Int, isFirst: Boolean = false, isLast: Boolean = false) {
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
        Column(
            modifier = Modifier
                .width(240.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.hotel_1_1), // Replace with your image resource
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
                    text = "Majestic Palza Hotel",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "London",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$100",
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


@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Group9_MAPD721Theme {
        //HomePage()
    }
}