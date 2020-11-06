package linc.com.weatherapp.utils

import java.text.DecimalFormat

object ProgressRounder {
    private val decimalFormat = DecimalFormat("#.##")

    fun roundProgress(progress: Float): Float {
        return decimalFormat.format(progress)
            .replace(",", ".")
            .toFloat()
    }

}