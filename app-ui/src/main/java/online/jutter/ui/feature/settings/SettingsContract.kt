package online.jutter.ui.feature.settings


class SettingsContract {

    data class State(
        val isLoading: Boolean = false
    )

    sealed class Effect {
        object DataWasLoaded : Effect()
    }
}