package online.jutter.ui.common.composable.fractal

import android.graphics.Color

/**
 * Описание многоцветного градиента для закрашивания фрактала.
 */
data class Gradient(
    val colors: List<RgbColor>
) {

    /**
     * Получение цвета в соответствии с интенсивностью точки на экране.
     */
    fun getColor(percent: Float): Int {
        val localPercent = percent * (colors.size - 1) - (percent * (colors.size - 1)).toInt()
        val firstColor = colors[(percent * (colors.size - 1)).toInt()]
        val secondColor = colors[(percent * (colors.size - 1)).toInt() + 1]

        val resultRed = firstColor.r + localPercent * (secondColor.r - firstColor.r)
        val resultGreen = firstColor.g + localPercent * (secondColor.g - firstColor.g)
        val resultBlue = firstColor.b + localPercent * (secondColor.b - firstColor.b)

        return Color.argb(1F, resultRed, resultGreen, resultBlue)
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
}

val GradientBlue = Gradient(
    listOf(
        RgbColor(0F, 0F, 0F),
        RgbColor(0F, 0F, 1F),
    )
)

val GradientBlueGreen = Gradient(
    listOf(
        RgbColor(0F, 0F, 0F),
        RgbColor(0F, 0F, 1F),
        RgbColor(0F, 1F, 1F),
    )
)

val GradientFractalysis = Gradient(
    listOf(
        RgbColor(255, 255, 255),
        RgbColor(136, 151, 169),
        RgbColor(129, 113, 95),
        RgbColor(0, 0, 0),
        RgbColor(129, 113, 95),
        RgbColor(136, 151, 169),
        RgbColor(255, 255, 255),
    )
)

val GradientDeepSpace = Gradient(
    listOf(
        RgbColor(0, 0, 0),
        RgbColor(94, 167, 255),
        RgbColor(0, 176, 200),
        RgbColor(109, 90, 211),
        RgbColor(255, 174, 174),
    )
)

val GradientRainbow = Gradient(
    listOf(
        RgbColor(0, 0, 0),
        RgbColor(0, 0, 255),
        RgbColor(0, 255, 255),
        RgbColor(255, 150, 0),
        RgbColor(0, 0, 0),
    )
)