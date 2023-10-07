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

    private var centerX: Double,
    private var centerY: Double,
    private var scale: Double,

    private var gradient: Gradient,
) {

    companion object {
        private var updateImageJob: Job? = null

    }

    private var fractal = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    private var fractalOutput = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    //private var fractalFunction = (, x)

    private var sectorIterations = listOf(7, 5, 3, 2, 1)
    private var iterations: Int = START_ITERATIONS

    private val runtime = Runtime.getRuntime()


    private var onImageUpdated: ((Bitmap) -> Unit)? = null
    private var onProgressUpdate: ((Float, Long, Long) -> Unit)? = null
    private var onPositionUpdate: ((Double, Double, Double)->Unit)? = null
    private var iterationsCountForProgress: Long = 0
    private var currentIterationsCount: Long = 0
    private var startTime = 0L
    private val pixelRenderJobs = mutableListOf<Job>()

    private var midLIterations = 0L
    private var midIterationsCount = 0L

    init {
        sectorIterations.forEach {
            iterationsCountForProgress += (width / 2.0.pow(it).toInt()) + 1
        }
    }

    fun transform(zoomChange: Float, offsetChange: Offset) {
        scale /= zoomChange.toDouble()
        centerX -= offsetChange.x.toDouble() * scale
        centerY -= offsetChange.y.toDouble() * scale
        onPositionUpdate?.invoke(centerX, centerY, scale)
        updateImage()
    }

    fun onPositionUpdate(listener: (Double, Double, Double)->Unit) {
        onPositionUpdate = listener
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
        pixelRenderJobs.clear()
        updateImageJob = GlobalScope.launchIO {
            val canvas = Canvas(fractal)
            val paint = Paint()

            startTime = System.currentTimeMillis()
            sectorIterations.forEach {
                drawLevel(canvas, paint, 2.0.pow(it).toInt())
                if (midLIterations / midIterationsCount / iterations.toFloat() != 0.05F) {
                    val iterations = ((midLIterations / midIterationsCount) * 5F).toInt()
                    if (iterations < 350) this.iterations = 350 else this.iterations = iterations
                }
                logDebug(FRACTAL_VIEWER_LOG,"Render level $it time: ${(System.currentTimeMillis() - startTime)/1000} sec. ${(System.currentTimeMillis() - startTime)%1000} ms.")
                withUI {
                    onImageUpdated?.invoke(fractal)
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
                val job = GlobalScope.launchIO {
                    val intensity = calcPixelIntensity(
                        Complex(
                            (x * (level) - width / 2.0) * scale + centerX,
                            (y * (level) - height / 2.0) * scale + centerY,
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
                pixelRenderJobs.add(job)
            }
            withUI { updateProgress(System.currentTimeMillis() - startTime) }
        }
    }

    private fun calcPixelIntensity(c: Complex): Float {
        var z = Complex(0, 0)
        for (i in 0 until iterations) {
            // z(n+1) = z(n)*z(n) + c
            z = z.square() + c
            if ((z.real() * z.real() + z.imag() * z.imag()) > 9.0) {
                midLIterations += i
                midIterationsCount += 1
                return i / START_ITERATIONS.toFloat()
            }
        }
        return 0F
    }

    private fun calcPixelColor(intensity: Float): Int {
        return gradient.getColor(intensity)
    }
}

const val START_ITERATIONS = 350
const val FRACTAL_VIEWER_LOG = "FractalAsyncViewer"