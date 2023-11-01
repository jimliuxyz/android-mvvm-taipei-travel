package com.example.taipeitravel.repository

import com.example.taipeitravel.models.Attraction
import com.example.taipeitravel.models.TravelPageResponse
import com.example.taipeitravel.webapi.TravelService
import javax.inject.Inject

class TravelRepository @Inject constructor(private val service: TravelService) : ITravelRepository {

    override suspend fun getAttractions(
        lang: String,
        page: Int
    ): TravelPageResponse<Attraction> {
        val res = service.listAttractions(lang, page)!!.execute()
        return res.body()!!
    }

}