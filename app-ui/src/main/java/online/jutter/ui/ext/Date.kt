package online.jutter.ui.ext

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

const val MILLIS_IN_SECOND = 1000
const val SECONDS_IN_MINUTE = 60
const val MINUTES_IN_HOUR = 60
const val HOURS_IN_DAY = 24

const val MILLIS_IN_DAY = MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toEpochMillis() =
    this.toEpochDay() * MILLIS_IN_DAY

object LocalDateFactory {

    @RequiresApi(Build.VERSION_CODES.O)
    fun ofEpochMillis(millis: Long): LocalDate =
        LocalDate.ofEpochDay(millis / MILLIS_IN_DAY)
}