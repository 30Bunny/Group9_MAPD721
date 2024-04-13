package com.mapd.group9_mapd721.model

data class HotelDetails(
    var hotelId: String,
    var hotelName: String,
    var address: String,
    var countryCode: String,
    var city: String,
    var rating: Float,
    var pricePerNight: Double,
    var facilities: List<String>,
    var description: String,
    var imageUrls: List<String>,
)
