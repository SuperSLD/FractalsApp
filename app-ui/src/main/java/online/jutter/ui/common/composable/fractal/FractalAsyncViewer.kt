package online.jutter.ui.common.composable.fractal

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import online.jutter.ui.ext.launchIO
import online.jutter.ui.ext.logDebug
import online.jutter.ui.ext.withUI
import java.math.BigDecimal
import kotlin.math.pow
import kotlin.math.sqrt

class FractalAsyncViewer(
    private val width: Int,
    private val height: Int,
) {

    private var fractal = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    private var fractalOutput = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    //private var fractalFunction = (, x)

    private var sectorIterations = 7
    private var iterations: Int = 350

    // Начальная позиция
    // [x: -0.4857724165499294, y: -0.04209991654388896, scale: 0.002696652170084606]
    // Позиция максимального зума (до фиксов)
    // [x: -1.7596913735842183, y: -0.013188483709530576, scale: 8.095181873089536E-18]
    private var centerX = BigDecimal(-0.4857724165499294)
    private var centerY = BigDecimal(-0.04209991654388896)
    private var scale = BigDecimal(0.002696652170084606)
    //private var scale = 0.0002

    private var updateImageJob: Job? = null
    private var onImageUpdated: ((Bitmap) -> Unit)? = null
    private var onProgressUpdate: ((Float, Long, Long) -> Unit)? = null
    private var iterationsCountForProgress: Long = 0
    private var currentIterationsCount: Long = 0
    private var startTime = 0L

    private var gradient = GradientDeepSpace

    private var midLIterations = 0L
    private var midIterationsCount = 0L

    init {
        for (level in sectorIterations downTo 1) {
            iterationsCountForProgress += (width / 2.0.pow(level).toInt()) + 1
        }
    }

    fun transform(zoomChange: Float, offsetChange: Offset) {
        scale /= BigDecimal(zoomChange.toDouble())
        centerX -= BigDecimal(offsetChange.x.toDouble()) * scale
        centerY -= BigDecimal(offsetChange.y.toDouble()) * scale
        updateImage()
    }

    fun onProgressUpdate(callback: ((Float, Long, Long) -> Unit)?) {
        onProgressUpdate = callback
    }

    private fun updateProgress(time: Long) {
        currentIterationsCount += 1
        onProgressUpdate?.invoke(
            (currentIterationsCount / iterationsCountForProgress.toFloat() * 10000).toInt() / 100F,
            time,
            iterations.toLong(),
        )
    }

    fun onImageUpdated(callback: (Bitmap)->Unit) {
        onImageUpdated = callback
    }

    @DelicateCoroutinesApi
    fun updateImage() {
        logDebug(FRACTAL_VIEWER_LOG,"Start updating image")
        currentIterationsCount = 0
        updateImageJob?.cancel()
        updateImageJob = GlobalScope.launchIO {
            val canvas = Canvas(fractal)
            val paint = Paint()

            startTime = System.currentTimeMillis()
            for (level in sectorIterations downTo 1) {
                drawLevel(canvas, paint, 2.0.pow(level).toInt())
                if (midLIterations / midIterationsCount / iterations.toFloat() != 0.05F && level > sectorIterations / 2) {
                    val iterations = ((midLIterations / midIterationsCount) * 5F).toInt()
                    if (iterations < 350) this.iterations = 350 else this.iterations = iterations
                }
                fractalOutput = fractal.copy(Bitmap.Config.ARGB_8888, false)
                logDebug(FRACTAL_VIEWER_LOG,"Render level $level time: ${(System.currentTimeMillis() - startTime)/1000} sec. ${(System.currentTimeMillis() - startTime)%1000} ms.")
                withUI {
                    onImageUpdated?.invoke(fractalOutput)
                }
            }

            logDebug(FRACTAL_VIEWER_LOG,"Full render time: ${(System.currentTimeMillis() - startTime)/1000} sec. ${(System.currentTimeMillis() - startTime)%1000} ms. [x: $centerX, y: $centerY, scale: $scale]")
        }
    }

    private suspend fun drawLevel(canvas: Canvas, paint: Paint, level: Int) {
        midLIterations = 35L
        midIterationsCount = 1L
        for (x in 0..width / level) {
            for (y in 0..height / level) {
                val intensity = calcPixelIntensity(
                    Complex(
                        BigDecimal(x * (level) - width / 2.0) * scale + centerX,
                        BigDecimal(y * (level) - height / 2.0) * scale + centerY,
                    )
                )
                paint.color = calcPixelColor(intensity)
                canvas.drawRect(
                    x.toFloat() * (level),
                    y.toFloat() * (level),
                    x.toFloat() * (level) + level,
                    y.toFloat() * (level) + level,
                    paint,
                )
            }
            withUI { updateProgress(System.currentTimeMillis() - startTime) }
        }
    }

    private fun calcPixelIntensity(c: Complex): Float {
        var z = Complex(0, 0)
        for (i in 0 until iterations) {
            // z(n+1) = z(n)*z(n) + c
            z = z.square() + c
            if (z.real() * z.real() + z.imag() * z.imag() > BigDecimal(9)) {
                midLIterations += i
                midIterationsCount += 1
                return i / iterations.toFloat()
            }
        }
        return 0F
    }

    private fun calcPixelColor(intensity: Float): Int {
        return gradient.getColor(intensity)
    }
}

const val FRACTAL_VIEWER_LOG = "FractalAsyncViewer"