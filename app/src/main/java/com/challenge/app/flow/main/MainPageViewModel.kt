package com.challenge.app.flow.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.challenge.app.base.BaseViewModel
import com.challenge.app.extensions.forceRefresh
import com.challenge.app.models.Beer
import com.challenge.app.repository.local.AppSettings
import com.challenge.app.repository.remote.Repository
import com.challenge.app.repository.remote.Response
import com.challenge.app.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainPageViewModel(
    private val repository: Repository,
    private val appSettings: AppSettings
) : BaseViewModel() {

    private val _listStateLiveData = MutableLiveData<ListState>(ListState.Init)
    val listStateLiveData: LiveData<ListState> = _listStateLiveData

    private val _effect = SingleLiveEvent<Effect>()
    val effect: SingleLiveEvent<Effect> = _effect

    init {
        getBeers()
    }

    fun getBeers() {
        _listStateLiveData.value = ListState.Loading

        viewModelScope.launch(Dispatchers.Main) {

            when (val response = repository.getBeers()) {
                is Response.Success -> {
                    _listStateLiveData.value = ListState.Loaded(
                        response.result.map {
                            it.apply {
                                isFavorite = appSettings.getBoolean(id.toString(), false)
                            }
                        }
                    )
                }
                is Response.Error -> {
                    _listStateLiveData.value = ListState.Error(response.exception)
                    _effect.value = Effect.ShowErrorMessage(response.exception)
                }
            }
        }
    }

    fun refreshBeers(pair: Pair<Int, Boolean>) = when (val state = listStateLiveData.value) {
        is ListState.Loaded -> {
            state.result.forEach { beer ->
                if (beer.id == pair.first
                    && beer.isFavorite != pair.second
                ) {
                    beer.isFavorite = pair.second
                    _listStateLiveData.forceRefresh()
                }
            }
        }
        else -> { //just do nothing
        }
    }
}


sealed class ListState {
    object Loading : ListState()
    object Init : ListState()
    data class Loaded(val result: List<Beer>) : ListState()
    data class Error(val ex: Throwable) : ListState()
}

sealed class Effect {
    data class ShowErrorMessage(val ex: Throwable) : Effect()
}
