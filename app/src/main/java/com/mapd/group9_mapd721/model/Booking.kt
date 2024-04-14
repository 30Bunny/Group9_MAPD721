package com.mapd.group9_mapd721.model

data class Booking(
    val checkInDate: String,
    val checkOutDate: String,
    val numberOfRooms: Int,
    val hotelName: String,
    val hotelCity: String,
    val hotelImage: String,
    val totalAmount: Double
)