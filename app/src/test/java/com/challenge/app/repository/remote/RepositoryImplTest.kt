package com.challenge.app.repository.remote

import com.challenge.app.models.Amount
import com.challenge.app.models.Beer
import com.challenge.app.models.Hop
import com.challenge.app.models.Malt
import com.challenge.app.repository.remote.api.WebServiceApi
import com.google.common.truth.Truth.assertThat
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mobline.data.enqueueResponse
import com.mobline.data.enqueueResponse400
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import java.io.IOException
import java.util.concurrent.TimeUnit

class RepositoryImplTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var client: OkHttpClient
    private lateinit var api: WebServiceApi
    private lateinit var repo: Repository

    @ExperimentalSerializationApi
    @Before
    fun setUp() {
        mockWebServer = MockWebServer()

        client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.SECONDS)
            .build()

        api = Retrofit.Builder()
            .client(client)
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(
                Json {
                    isLenient = true
                    encodeDefaults = true
                    ignoreUnknownKeys = true
                    prettyPrint = true
                }.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(WebServiceApi::class.java)

        repo = RepositoryImpl(api)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        mockWebServer.shutdown()
    }


    @Test
    fun `on response 200 returns success and is equal to predefined list`() {
        mockWebServer.enqueueResponse("response-200.json", 200)

        runBlocking {
            val response = repo.getBeers()

            assertThat(response).isEqualTo(
                Response.Success(
                    listOf(
                        Beer(
                            id = 1,
                            isFavorite = false,
                            name = "Buzz",
                            description = "A light, crisp and bitter",
                            imageUrl = "https://images.punkapi.com/v2/keg.png",
                            abv = "4.5",
                            malt = listOf(
                                Malt(
                                    name = "Maris Otter Extra Pale",
                                    amount = Amount(
                                        value = "3.3",
                                        unit = "kilograms"
                                    )
                                )
                            ),
                            hops = listOf(
                                Hop(
                                    name = "Fuggles",
                                    amount = Amount(
                                        value = "25",
                                        unit = "grams"
                                    ),
                                    add = "start",
                                    attribute = "bitter"
                                )
                            ),
                            foodPairing = listOf(
                                "Spicy chicken tikka masala",
                                "Grilled chicken quesadilla"
                            )
                        ),
                        Beer(
                            id = 2,
                            isFavorite = false,
                            name = "Buzz_2",
                            description = "A light, crisp and bitter",
                            imageUrl = "https://images.punkapi.com/v2/keg.png",
                            abv = "4.5",
                            malt = listOf(
                                Malt(
                                    name = "Maris Otter Extra Pale",
                                    amount = Amount(
                                        value = "3.3",
                                        unit = "kilograms"
                                    )
                                )
                            ),
                            hops = listOf(
                                Hop(
                                    name = "Fuggles",
                                    amount = Amount(
                                        value = "25",
                                        unit = "grams"
                                    ),
                                    add = "start",
                                    attribute = "bitter"
                                )
                            ),
                            foodPairing = listOf(
                                "Spicy chicken tikka masala",
                                "Grilled chicken quesadilla"
                            )
                        ),
                    )
                )
            )
        }
    }

    @Test
    fun `on response 200 returns success and is not equal to predefined list`() {
        mockWebServer.enqueueResponse("response-200.json", 200)

        runBlocking {
            val response = repo.getBeers()

            assertThat(response).isNotEqualTo(
                Response.Success(
                    listOf(
                        Beer(
                            id = 1,
                            isFavorite = true,
                            name = "Buzz",
                            description = "A light, crisp and bitter",
                            imageUrl = "https://images.punkapi.com/v2/keg.png",
                            abv = "4.5",
                        )
                    )
                )
            )
        }
    }


    @Test
    fun `on response 400 returns error is true`() {
        mockWebServer.enqueueResponse400()

        runBlocking {
            val response = repo.getBeers()
            assertThat(response is Response.Error).isTrue()
        }
    }

    @Test
    fun `on response 400 returns success is false`() {
        mockWebServer.enqueueResponse400()

        runBlocking {
            val response = repo.getBeers()
            assertThat(response is Response.Success).isFalse()
        }
    }
}