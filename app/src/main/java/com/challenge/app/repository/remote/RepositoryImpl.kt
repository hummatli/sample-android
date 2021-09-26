package com.challenge.app.repository.remote

import com.challenge.app.models.Beer
import com.challenge.app.repository.remote.api.WebServiceApi

class RepositoryImpl(private val api: WebServiceApi) : Repository {

    override suspend fun getBeers(): Response<List<Beer>> {
        return try {
            Response.Success(api.getBeers().map { it.toDomain() })
        } catch (exception: Exception) {
            Response.Error(exception)
        }
    }
}

sealed class Response<T> {
    data class Success<T>(val result: T): Response<T>()
    data class Error<T>(val exception: Exception): Response<T>()
}
