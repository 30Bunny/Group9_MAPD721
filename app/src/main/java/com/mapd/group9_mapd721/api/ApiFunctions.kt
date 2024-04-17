package com.mapd.group9_mapd721.api

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.mapd.group9_mapd721.model.HotelDetails
import com.mapd.group9_mapd721.model.HotelListing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object ApiFunctions {
    val RAPID_API_KEY = "ae7cac4bc3msh045ee1f4f977080p15d03ejsna1e7c08a7996"
    //val RAPID_API_KEY = "b1ac5d66c0msh91f2fc3b1e0d478p154bbejsn31bea3e057b4"
    val RAPID_API_HOST = "booking-com.p.rapidapi.com"

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchHotels(
        destId: String = "38",
        destType: String = "country",
        onSuccess: (List<HotelListing>) -> Unit, onError: (String) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()

            // Calculate current date and next date
            val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val nextDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            val url =
                "https://booking-com.p.rapidapi.com/v1/hotels/search?checkout_date=$nextDate&order_by=popularity&filter_by_currency=CAD&room_number=1&dest_id=$destId&dest_type=$destType&adults_number=2&checkin_date=$currentDate&locale=en-us&units=metric"
            Log.d("MYURL", url)

            val request = Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Key", RAPID_API_KEY)
                .addHeader("X-RapidAPI-Host", RAPID_API_HOST)
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
                val price =
                    decimalFormat.format(hotelObject.optDouble("min_total_price", 0.0)).toDouble()
                val city = hotelObject.optString("city", "")

                val hotelListing = HotelListing(hotelId, hotelName, hotelImage, price, city)
                //Log.d("List", hotelListing.toString())
                hotels.add(hotelListing)
            }
        }
        Log.d("List", hotels.toString())
        return hotels
    }

    fun parseResponse(
        responseBody: String?,
        onSuccess: (String, String) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            // Check if response body is not null or empty
            if (!responseBody.isNullOrBlank()) {
                // Parse the JSON array from the response body
                val jsonArray = JSONArray(responseBody)

                // Check if the array is not empty
                if (jsonArray.length() > 0) {
                    // Get the first object from the array
                    val firstObject = jsonArray.getJSONObject(0)

                    // Extract the `name` and `dest_type` properties from the first object
                    val destId = firstObject.optString("dest_id", "")
                    val destType = firstObject.optString("dest_type", "")

                    // Call the onSuccess callback with the extracted data
                    onSuccess(destId, destType)
                } else {
                    onError("No data available in the response array")
                }
            } else {
                onError("Empty response body")
            }
        } catch (e: Exception) {
            onError("Error parsing response: ${e.message}")
        }
    }

    fun getDestinationId(
        query: String,
        onSuccess: (String, String) -> Unit,
        onError: (String) -> Unit
    ) {
        val client = OkHttpClient()

        if (query.isBlank()) {
            // If searchText is empty, use default values
            onSuccess("38", "country")
            return
        }

        val request = Request.Builder()
            .url("https://booking-com.p.rapidapi.com/v1/hotels/locations?name=$query&locale=en-us")
            .get()
            .addHeader("X-RapidAPI-Key", RAPID_API_KEY)
            .addHeader("X-RapidAPI-Host", RAPID_API_HOST)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError("Failed to make API call: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    onError("Failed to get response: ${response.code}")
                    return
                }

                val responseBody = response.body?.string()
                parseResponse(
                    responseBody,
                    onSuccess = { destId, destType ->
                        Log.d("Destination ID:", destId)
                        Log.d("Destination type:", destType)
                        onSuccess(destId, destType)
                    },
                    onError = onError
                )
            }
        })
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
}
