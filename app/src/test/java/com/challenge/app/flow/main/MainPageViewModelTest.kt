package com.challenge.app.flow.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.challenge.app.extensions.CoroutineRule
import com.challenge.app.extensions.getOrAwaitValueTest
import com.challenge.app.models.Amount
import com.challenge.app.models.Beer
import com.challenge.app.models.Hop
import com.challenge.app.models.Malt
import com.challenge.app.repository.remote.Response
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import java.lang.Exception


class MainPageViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    private lateinit var viewModel: MainPageViewModel


    @ExperimentalCoroutinesApi
    @Test
    fun `get beers from repo with success case, state should equal Loaded with the same input`() {
        val input = listOf(
            Beer(
                id = 1,
                isFavorite = true,
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
                isFavorite = true,
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

        val repoApi = FakeRepositoryImpl(Response.Success(input))
        val appSettings = FakeAppSettingsImpl2()
        viewModel = MainPageViewModel(repoApi, appSettings)


        viewModel.getBeers()

        coroutinesTestRule.pauseDispatcher()

        assertThat(viewModel.listStateLiveData.getOrAwaitValueTest()).isEqualTo(
            ListState.Loaded(
                input
            )
        )

        coroutinesTestRule.resumeDispatcher()
    }


    @ExperimentalCoroutinesApi
    @Test
    fun `get beers from repo with error case, state is equal Error in view model`() {

        val ex = Exception("sample ex")
        val repo = FakeRepositoryImpl(Response.Error(ex))
        val appSettings = FakeAppSettingsImpl2()
        viewModel = MainPageViewModel(repo, appSettings)


        viewModel.getBeers()

        coroutinesTestRule.pauseDispatcher()

        assertThat(viewModel.listStateLiveData.getOrAwaitValueTest()).isEqualTo(ListState.Error(ex))

        coroutinesTestRule.resumeDispatcher()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `get beers from repo with error case, effect is equal to ShowErrorMessage in view model`() {

        val ex = Exception("sample ex")
        val repo = FakeRepositoryImpl(Response.Error(ex))
        val appSettings = FakeAppSettingsImpl2()
        viewModel = MainPageViewModel(repo,appSettings)


        viewModel.getBeers()

        coroutinesTestRule.pauseDispatcher()

        assertThat(viewModel.effect.getOrAwaitValueTest()).isEqualTo(Effect.ShowErrorMessage(ex))

        coroutinesTestRule.resumeDispatcher()
    }
}