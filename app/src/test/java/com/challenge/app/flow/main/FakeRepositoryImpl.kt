package com.challenge.app.flow.main

import com.challenge.app.models.Beer
import com.challenge.app.repository.remote.Repository
import com.challenge.app.repository.remote.Response

class FakeRepositoryImpl(val response: Response<List<Beer>>): Repository {
    override suspend fun getBeers(): Response<List<Beer>> {
        return response
    }
}