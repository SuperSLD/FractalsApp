package online.jutter.ui.feature.home


class HomeContract {

    data class State(
        val isLoading: Boolean = false
    )

    sealed class Effect {
        object DataWasLoaded : Effect()
    }
}