package com.example.taipeitravel.webapi

import com.example.taipeitravel.models.Attraction
import com.example.taipeitravel.models.TravelPageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface TravelService {
    @Headers("accept: application/json")
    @GET("{lang}/Attractions/All")
    fun listAttractions(
        @Path("lang") lang: String,
        @Query("page") page: Int,
        @Query("categoryIds") categoryIds: Int = 13
    ): Call<TravelPageResponse<Attraction>>?
}