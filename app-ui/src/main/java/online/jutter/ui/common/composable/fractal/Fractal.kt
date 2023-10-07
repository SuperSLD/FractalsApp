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

// Начальная позиция
// [x: -0.4857724165499294, y: -0.04209991654388896, scale: 0.002696652170084606]
// Позиция максимального зума (до фиксов)
// [x: -1.7596913735842183, y: -0.013188483709530576, scale: 8.095181873089536E-18]
// Красивая загагулина
// [x: -0.2343347114572282, y: 0.8271799783391012, scale: 1.482389478882432E-9]

@Composable
fun Fractal(
    centerX: Double = -0.4857724165499294,
    centerY: Double = -0.04209991654388896,
    fractalScale: Double = 0.002696652170084606,
//    centerX: Double = -0.2343347114572282,
//    centerY: Double = 0.8271799783391012,
//    fractalScale: Double = 1.482389478882432E-9,
    gradient: Gradient = GradientBlueGreen,
    onPositionUpdate: ((Double, Double, Double) -> Unit)? = null,
    onProgressUpdate: ((Float, Long, Long) -> Unit)? = null,
    onBitmapChanged: ((Bitmap)->Unit)? = null,
) {

    var fractalBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var fractalAsyncViewer by remember { mutableStateOf<FractalAsyncViewer?>(null) }
    var composableIsPositioned by remember { mutableStateOf(false) }

    var transition by remember { mutableStateOf(false) }
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        if (!transition) {
            transition = true
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
                        width = coordinates.size.width,
                        height = coordinates.size.height,
                        centerX = centerX,
                        centerY = centerY,
                        scale = fractalScale,
                        gradient = gradient,
                    ).apply {
                        onProgressUpdate(onProgressUpdate)
                        onImageUpdated {
                            transition = false
                            fractalBitmap = it
                            onBitmapChanged?.invoke(it)
                        }
                        onPositionUpdate?.let { onPositionUpdate(it) }
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
                bitmap = fractalBitmap,
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