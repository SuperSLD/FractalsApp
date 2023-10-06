package online.jutter.ui.common.composable.fractal

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.min
import kotlin.math.pow

/**
 * Мда, никогда не думал что придется
 * реализовывать класс дробных чисел
 * потому, что чертов double не справляется.
 */
class Fraction {

    val cutLine = Long.MAX_VALUE.toString().length
    val cutLineSize = 10.0.pow(cutLine)

    var n: Long = 1
    var d: Long = 1

    constructor(n: Long, d: Long) {
        if (n > d && n > cutLineSize) {
            val nl = n.toString().length
            val det = nl - cutLine
            this.n = n / det
            this.d = d / det
        } else if (n < d && d > cutLineSize) {
            val dl = d.toString().length
            val det = dl - cutLine
            this.n = n / det
            this.d = d / det
        } else {
            this.n = n
            this.d = d
        }
    }

    constructor(n: Int, d: Int) {
        this.n = n.toLong()
        this.d = d.toLong()
    }

    constructor(c: Int) {
        this.n = c.toLong()
        this.d = 1
    }

    constructor(d: Double) {
        var localD = d
        var c = 1L
        while ((localD * 10).toInt() % 10 != 0) {
            c *= 10L
            localD = localD * 10 % 10
        }
        this.n = ((d % 1) * c).toLong()
        this.d = c
    }

    override fun toString(): String {
        val strF = Fraction(n, d)//.simplify()
        return if (strF.d == 1L) strF.n.toString() else if (n == 0L) "0" else "${strF.n}/${strF.d}"
    }

    fun simplify(): Fraction {
        val limit = min(this.n, this.d)
        for (i in 2..limit) {
            if (this.n % i == 0L && this.d % i == 0L) {
                this.n /= i
                this.d /= i
            }
        }
        return this
    }

    operator fun plus(other: Fraction): Fraction {
        return if (d != other.d) Fraction(other.n * this.d + this.n * other.d, this.d * other.d)
        else Fraction(other.n + n, d)
    }

    operator fun plus(i: Int): Fraction {
        return Fraction(n + (d * i), d)
    }

    operator fun minus(other: Fraction): Fraction {
        return if (d != other.d) Fraction(this.n * other.d - other.n * this.d, this.d * other.d)
        else Fraction(n - other.n, d)
    }

    operator fun minus(i: Int): Fraction {
        return Fraction(n- (i * d), d)
    }

    operator fun times(other: Fraction): Fraction {
        return Fraction(n * other.n, d * other.d)
    }

    operator fun times(i: Int): Fraction {
        return Fraction(n * i, d)
    }

    operator fun div(other: Fraction): Fraction {
        return Fraction(n * other.d, d * other.n)
    }

    operator fun div(i: Int): Fraction {
        return Fraction(n / i, d)
    }

    companion object {

        fun zero() = Fraction(0, 1)
    }
}

fun Double.toFraction() = Fraction(this)

fun Fraction.toDouble(n: Int): Double {
    val strF = Fraction(this.n, this.d).simplify()
    return (strF.n.toDouble() / strF.d).round(n)
}

fun Fraction.toDouble(): Double {
    val strF = Fraction(this.n, this.d).simplify()
    return (strF.n / strF.d.toDouble())
}

fun Double.round(n: Int) = this.toBigDecimal().setScale(n, RoundingMode.CEILING).toDouble()