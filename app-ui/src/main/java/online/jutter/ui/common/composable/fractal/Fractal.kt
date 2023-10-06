package online.jutter.ui.common.composable.fractal

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import online.jutter.ui.common.composable.BitmapImage
import online.jutter.ui.ext.logDebug
import online.jutter.ui.theme.FractalsTheme

@Composable
fun Fractal(
    iterations: Int = 100,
    onProgressUpdate: ((Float, Long, Long) -> Unit)? = null
) {

    var fractalBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var lastFractalBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var fractalAsyncViewer by remember { mutableStateOf<FractalAsyncViewer?>(null) }
    var composableIsPositioned by remember { mutableStateOf(false) }

    var transition by remember { mutableStateOf(false) }
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        if (!transition) {
            transition = true
            lastFractalBitmap = fractalBitmap?.copy(Bitmap.Config.ARGB_8888, false)
            offset = Offset(0F, 0F)
            scale = 1f
        }
        fractalAsyncViewer?.transform(zoomChange, offsetChange)
        scale *= zoomChange
        offset += offsetChange
    }

    Box(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                logDebug(FRACTAL_VIEWER_LOG, "onPlaced")
                if (!composableIsPositioned) {
                    composableIsPositioned = true
                    fractalAsyncViewer = FractalAsyncViewer(
                        coordinates.size.width, coordinates.size.height,
                    ).apply {
                        onProgressUpdate(onProgressUpdate)
                        onImageUpdated {
                            transition = false
                            fractalBitmap = it
                        }
                        updateImage()
                    }
                }

            }
            .transformable(state = state)
            .fillMaxSize()
            .background(Color.Black),
    ) {
        if (!transition) {
            BitmapImage(
                bitmap = fractalBitmap,
            )
        } else {
            BitmapImage(
                bitmap = lastFractalBitmap,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
            )
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 500,
    heightDp = 900,
)
@Composable
fun DefaultPreview() {
    FractalsTheme {
        Fractal()
    }
}