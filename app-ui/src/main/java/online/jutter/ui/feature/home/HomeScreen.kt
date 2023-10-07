package online.jutter.ui.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import online.jutter.ui.NavigationKeys
import online.jutter.ui.common.composable.RadialGradientPreview
import online.jutter.ui.common.composable.fractal.Fractal
import online.jutter.ui.common.composable.fractal.GradientBlueGreen
import online.jutter.ui.theme.Background
import online.jutter.ui.theme.FractalsTheme

val currentGradient = GradientBlueGreen

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

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Transparent
    )

    var progress by remember {
        mutableFloatStateOf(0F)
    }
    var time by remember {
        mutableLongStateOf(0L)
    }
    var iterations by remember {
        mutableLongStateOf(0L)
    }

    Scaffold { innerPadding ->
        val contentModifier = Modifier
            .padding(innerPadding)

        if (state.isLoading) {
            LoadingBar()
        } else {
            Fractal(
                gradient = currentGradient,
                onProgressUpdate = { p, t, i ->
                    progress = p
                    time = t
                    iterations = i
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .statusBarsPadding()
                    .navigationBarsPadding(),
            ) {
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Card(
                            modifier = Modifier
                                .width(48.dp)
                                .height(48.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Background,
                            ),
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                RadialGradientPreview(
                                    gradient = currentGradient,
                                    modifier = Modifier
                                        .width(36.dp)
                                        .height(36.dp),
                                )
                            }
                        }
                        Spacer(modifier = Modifier.weight(1F))
                        AnimatedVisibility(visible = progress == 100F) {
                            Card(
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(48.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Background,
                                ),
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize(),
                                ) {
                                    Icon(
                                        Icons.Default.Share,
                                        contentDescription = null,
                                        tint = currentGradient.accent(),
                                    )
                                }
                            }
                        }
                        AnimatedVisibility(visible = progress < 100F) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Card(
                                    modifier = Modifier
                                        .width(78.dp)
                                        .height(48.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Background,
                                    ),
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.fillMaxSize(),
                                    ) {
                                        Text(
                                            text = "$progress%",
                                            color = Color.White,
                                            //modifier = Modifier.fillMaxSize(),
                                            //textAlign = TextAlign.,
                                        )
                                    }
                                }
                                Card(
                                    modifier = Modifier
                                        .width(48.dp)
                                        .height(48.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Background,
                                    ),
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.fillMaxSize(),
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier
                                                .width(24.dp)
                                                .height(24.dp),
                                            color = currentGradient.accent(),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
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

//
//@Preview(
//    showBackground = true,
//    uiMode = UI_MODE_NIGHT_YES
//)
//@Composable
//fun DarkDefaultPreview() {
//    FractalsTheme {
//        HomeScreen(HomeContract.State(), null, { })
//    }
//}