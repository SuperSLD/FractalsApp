package online.jutter.ui.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.delay
import online.jutter.ui.common.mvvm.BaseViewModel
import online.jutter.ui.ext.launchUI

class HomeViewModel: BaseViewModel() {

    var state by mutableStateOf(
        HomeContract.State(
            isLoading = false
        )
    )
        private set

    var effects = Channel<HomeContract.Effect>(UNLIMITED)
        private set

    init {
        viewModelScope.launchUI { getFoodCategories() }
    }

    private suspend fun getFoodCategories() {
        delay(1000)
        effects.send(HomeContract.Effect.DataWasLoaded)
        //val categories = remoteSource.getFoodCategories()
//        viewModelScope.launch {
//            state = state.copy(categories = categories, isLoading = false)
//            effects.send(HomeViewModel.Effect.DataWasLoaded)
//        }
    }
}



