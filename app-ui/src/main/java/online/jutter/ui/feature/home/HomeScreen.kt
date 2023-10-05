package online.jutter.ui.feature.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import online.jutter.ui.NavigationKeys
import online.jutter.ui.common.composable.fractal.Fractal
import online.jutter.ui.theme.FractalsTheme

@Composable
fun HomeScreenDestination(navController: NavHostController) {
    val viewModel = HomeViewModel()
    HomeScreen(
        state = viewModel.state,
        effectFlow = viewModel.effects.receiveAsFlow(),
        onNavigationRequested = {
            navController.navigate(NavigationKeys.Route.HOME_SCREEN)
        }
    )
}

@Composable
fun HomeScreen(
    state: HomeContract.State,
    effectFlow: Flow<HomeContract.Effect>?,
    onNavigationRequested: () -> Unit
) {

    var progress by remember {
        mutableFloatStateOf(0F)
    }

    Scaffold { innerPadding ->
        val contentModifier = Modifier
            .padding(innerPadding)

        if (state.isLoading) {
            LoadingBar()
        } else {
            Fractal(onProgressUpdate = { progress = it })
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(16.dp),
            ) {
                Text(
                    text = "Loading: $progress%",
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
fun LoadingBar() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        CircularProgressIndicator()
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun DefaultPreview() {
    FractalsTheme {
        HomeScreen(HomeContract.State(), null, { })
    }
}


@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun DarkDefaultPreview() {
    FractalsTheme {
        HomeScreen(HomeContract.State(), null, { })
    }
}