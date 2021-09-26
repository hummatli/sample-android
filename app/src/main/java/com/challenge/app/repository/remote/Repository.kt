package com.challenge.app.repository.remote

import com.challenge.app.models.Beer

interface Repository {
    suspend fun getBeers(): Response<List<Beer>>
}
