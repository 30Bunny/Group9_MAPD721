package com.mapd.group9_mapd721.model

data class PlaceListItem(val imageUrl: String, val name: String)

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