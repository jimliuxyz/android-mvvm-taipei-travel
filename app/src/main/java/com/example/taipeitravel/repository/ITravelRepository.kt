package com.example.taipeitravel.repository

import com.example.taipeitravel.models.Attraction
import com.example.taipeitravel.models.TravelPageResponse

interface ITravelRepository {
    suspend fun getAttractions(lang: String, page: Int): TravelPageResponse<Attraction>
}