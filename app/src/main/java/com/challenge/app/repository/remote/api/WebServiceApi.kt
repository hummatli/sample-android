package com.challenge.app.repository.remote.api

import com.challenge.app.models.remote.BeerRes
import retrofit2.http.GET

interface WebServiceApi {
    @GET("/v2/beers")
    suspend fun getBeers(): List<BeerRes>
}
