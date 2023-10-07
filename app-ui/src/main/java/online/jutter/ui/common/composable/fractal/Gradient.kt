package online.jutter.ui.common.composable.fractal

import android.graphics.Color

/**
 * Описание многоцветного градиента для закрашивания фрактала.
 */
data class Gradient(
    val colors: List<RgbColor>,
    val name: String,
) {

    private val accentColor: RgbColor

    init {
        var r = 0F
        var g = 0F
        var b = 0F
        var count = 0
        colors.forEach { color ->
            if (!color.isMonotone()) {
                r += color.r
                g += color.g
                b += color.b
                count++
            }
        }
        accentColor = if (count == 0) {
            RgbColor(1F, 1F, 1F)
        } else {
            RgbColor(r / count, g / count, b / count)
        }
    }

    /**
     * Получение цвета в соответствии с интенсивностью точки на экране.
     */
    fun getColor(percent: Float): Int {
        val localPercent = percent % 1F * (colors.size - 1) - (percent % 1F * (colors.size - 1)).toInt()
        val firstColor = colors[(percent % 1F * (colors.size - 1)).toInt()]
        val secondColor = colors[(percent % 1F * (colors.size - 1)).toInt() + 1]

        val resultRed = firstColor.r + localPercent * (secondColor.r - firstColor.r)
        val resultGreen = firstColor.g + localPercent * (secondColor.g - firstColor.g)
        val resultBlue = firstColor.b + localPercent * (secondColor.b - firstColor.b)

        return Color.argb(1F, resultRed, resultGreen, resultBlue)
    }

    /**
     * Получение акцентного цвета градиента.
     */
    fun accent() = with(accentColor) {
        androidx.compose.ui.graphics.Color(r, g, b, 1F)
    }
}

/**
 * RGB модель цвета, для описания градиента.
 */
data class RgbColor(
    val r: Float,
    val g: Float,
    val b: Float,
) {
    constructor(r: Int, g: Int, b: Int) : this(r/255F, g/255F, b/255F)

    fun isWhite() = r == 1F && g == 1F && b == 1F

    fun isBlack() = r == 0F && g == 0F && b == 0F

    fun isMonotone() = r == g && g == b
}

val GradientBlue = Gradient(
    colors = listOf(
        RgbColor(0F, 0F, 0F),
        RgbColor(0F, 0F, 1F),
        RgbColor(0F, 0F, 0F),
    ),
    name = "Deep Blue",
)

val GradientBlueGreen = Gradient(
    colors = listOf(
        RgbColor(0F, 0F, 0F),
        RgbColor(0F, 0F, 1F),
        RgbColor(0F, 1F, 1F),
        RgbColor(0F, 0F, 1F),
        RgbColor(0F, 0F, 0F),
    ),
    name = "Blue To Green",
)

val GradientBlueRedGreen = Gradient(
    colors = listOf(
        RgbColor(0F, 0F, 0F),
        RgbColor(0F, 0F, 1F),
        RgbColor(1F, 0F, 1F),
        RgbColor(0F, 1F, 1F),
        RgbColor(1F, 0F, 1F),
        RgbColor(0F, 0F, 1F),
        RgbColor(0F, 0F, 0F),
    ),
    name = "Blue To Green With Red",
)


val GradientFractalysis = Gradient(
    colors = listOf(
        RgbColor(255, 255, 255),
        RgbColor(136, 151, 169),
        RgbColor(129, 113, 95),
        RgbColor(0, 0, 0),
        RgbColor(129, 113, 95),
        RgbColor(136, 151, 169),
        RgbColor(255, 255, 255),
    ),
    name = "From Fractalysis App",
)

val GradientDeepSpace = Gradient(
    colors = listOf(
        RgbColor(0, 0, 0),
        RgbColor(94, 167, 255),
        RgbColor(0, 176, 200),
        RgbColor(109, 90, 211),
        RgbColor(255, 174, 174),
        RgbColor(0, 0, 0),
    ),
    name = "Deep Space",
)

val GradientRainbow = Gradient(
    colors = listOf(
        RgbColor(0, 0, 0),
        RgbColor(0, 0, 255),
        RgbColor(0, 255, 255),
        RgbColor(255, 150, 0),
        RgbColor(0, 0, 0),
    ),
    name = "Rainbow",
)

val GradientMonotoneBlack = Gradient(
    colors = listOf(
        RgbColor(0, 0, 0),
        RgbColor(255, 255, 255),
        RgbColor(0, 0, 0),
    ),
    name = "Monotone Black",
)