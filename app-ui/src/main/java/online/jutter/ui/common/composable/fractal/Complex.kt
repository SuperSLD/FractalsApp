package online.jutter.ui.common.composable.fractal

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Косплексное число.
 *
 * Математические операции для работы с комплексными числами.
 */
class Complex(
    private var real: Double,
    private var imag: Double,
) {

    companion object {
        const val COMPLEX_SCALE = 200
    }

    constructor(real: Int, imag: Int) : this(real = real.toDouble(), imag = imag.toDouble())

    /**
     * Получение реальной части комплексного числа
     */
    fun real() = real

    /**
     * Получение мнимой части комплексного числа
     */
    fun imag() = imag

    operator fun plus(other: Complex): Complex {
        this.imag += other.imag
        this.real += other.real
        return this
    }

    operator fun minus(other: Complex): Complex {
        this.imag -= other.imag
        this.real -= other.imag
        return this
    }

    operator fun times(other: Complex): Complex {
        val real = real
        val imag = imag

        this.real = real * other.real() - imag * other.imag()
        this.imag = real * other.imag() + imag * other.real()
        return this
    }

    operator fun div(other: Complex): Complex {
        val real = real
        val imag = imag

        this.real = (real * other.real() + imag * other.imag()) /
                (other.real() * other.real() + other.imag() * other.imag())
        this.imag = (real * other.imag() + other.real() * imag) /
                (other.real() * other.real() + other.imag() * other.imag())
        return this
    }

    fun square(): Complex {
        val real = real
        val imag = imag

        this.real = (real * real - imag * imag)
        this.imag = (2.0 * imag * real)
        return this
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Complex) real == other.real() && imag == other.imag() else false
    }

    override fun hashCode(): Int {
        var result = real.hashCode()
        result = 31 * result + imag.hashCode()
        return result
    }
}