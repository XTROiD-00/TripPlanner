package com.example.budgetx.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class TripRequest(
    val name: String,
    val destination: String,
    val start_date: String,
    val end_date: String,
    val notes: String? = null
)

data class TripResponse(
    val id: Long,
    val name: String,
    val destination: String,
    val start_date: String?,
    val end_date: String?,
    val notes: String?
)

interface TripApiService {

    @GET("api/trips")
    suspend fun getTrips(): List<TripResponse>

    @POST("api/trips")
    suspend fun createTrip(
        @Body trip: TripRequest
    ): TripResponse
}