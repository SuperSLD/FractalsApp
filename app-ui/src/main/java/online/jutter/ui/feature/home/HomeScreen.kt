package online.jutter.ui.feature.home

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import online.jutter.ui.NavigationKeys
import online.jutter.ui.common.composable.BottomSheet
import online.jutter.ui.common.composable.RadialGradientPreview
import online.jutter.ui.common.composable.fractal.Fractal
import online.jutter.ui.common.composable.fractal.GradientBlue
import online.jutter.ui.common.composable.fractal.GradientBlueGreen
import online.jutter.ui.common.composable.fractal.GradientBlueRedGreen
import online.jutter.ui.common.composable.fractal.GradientDeepSpace
import online.jutter.ui.common.composable.fractal.GradientFractalysis
import online.jutter.ui.common.composable.fractal.GradientMonotoneBlack
import online.jutter.ui.theme.Background
import online.jutter.ui.theme.BackgroundSecondary
import online.jutter.ui.theme.FractalsTheme
import online.jutter.ui.theme.TextGray


@ExperimentalMaterial3Api
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

@ExperimentalMaterial3Api
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

    var progress by remember { mutableFloatStateOf(0F) }
    var time by remember { mutableLongStateOf(0L) }
    var iterations by remember { mutableLongStateOf(0L) }
    var fractalBitmapLastUpdate by remember { mutableLongStateOf(0L) }
    var fractalBitmap by remember { mutableStateOf<Bitmap?>(null) }

    var centerX by remember { mutableDoubleStateOf(-0.4857724165499294) }
    var centerY by remember { mutableDoubleStateOf(-0.04209991654388896) }
    var fractalScale by remember { mutableDoubleStateOf(0.002696652170084606) }
    var gradient by remember { mutableStateOf(GradientBlueGreen) }

    var showSheet by remember { mutableStateOf(false) }

    var gradientList by remember {
        mutableStateOf(
            listOf(
                GradientBlueGreen,
                GradientBlue,
                GradientDeepSpace,
                GradientFractalysis,
                GradientBlueRedGreen,
                GradientMonotoneBlack,
            )
        )
    }

    val context = LocalContext.current

    Scaffold { innerPadding ->
        Modifier.padding(innerPadding)

        if (showSheet) {
            BottomSheet(onDismiss = {showSheet = false}) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                ) {
                    items(gradientList) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = BackgroundSecondary,
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 8.dp
                            ),
                            onClick = {
                                gradient = it
                                showSheet = false
                            }
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                RadialGradientPreview(
                                    gradient = it,
                                    iterations = 40,
                                    modifier = Modifier
                                        .width(64.dp)
                                        .height(64.dp)
                                )
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxSize(),
                                ) {
                                    Text(
                                        text = it.name,
                                        color = Color.White,
                                        fontSize = 20.sp
                                    )
                                    Text(
                                        text = "Color count: ${it.colors.size}",
                                        color = TextGray,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (state.isLoading) {
            LoadingBar()
        } else {
            key(gradient) {
                Fractal(
                    centerX = centerX,
                    centerY = centerY,
                    fractalScale = fractalScale,
                    gradient = gradient,
                    onProgressUpdate = { p, t, i ->
                        progress = p
                        time = t
                        iterations = i
                    },
                    onBitmapChanged = {
                        fractalBitmapLastUpdate = System.currentTimeMillis()
                        fractalBitmap = it
                    },
                    onPositionUpdate = { x, y, scale ->
                        centerX = x
                        centerY = y
                        fractalScale = scale
                    }
                )
            }
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
                            onClick = {
                                showSheet = true
                            },
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                RadialGradientPreview(
                                    gradient = gradient,
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
                                onClick = {
                                    sharePalette(context, fractalBitmap!!)
                                }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize(),
                                ) {
                                    Icon(
                                        Icons.Default.Share,
                                        contentDescription = null,
                                        tint = gradient.accent(),
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
                                            color = gradient.accent(),
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

private fun sharePalette(context: Context, bitmap: Bitmap) {
    val bitmapPath = MediaStore.Images.Media.insertImage(
        context.contentResolver,
        bitmap,
        "Fractal",
        "generated in Fractal App"
    )
    val bitmapUri = Uri.parse(bitmapPath)
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "image/png"
    intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
    context.startActivity(Intent.createChooser(intent, "Share"))
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

@OptIn(ExperimentalMaterial3Api::class)
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