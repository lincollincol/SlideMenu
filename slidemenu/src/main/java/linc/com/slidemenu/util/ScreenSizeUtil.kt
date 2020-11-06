package linc.com.weatherapp.utils

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

object ScreenSizeUtil {

    private lateinit var context: Context
    private var height: Int = 0
    private var width: Int = 0

    fun init(context: Context) {
        this.context = context
        calculateSize()
    }

    fun calculateSize() {
        val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val size = Point()
        display.getSize(size)
        this.width = size.x
        this.height = size.y
    }

    fun getHeight() = height
    fun getWidth() = width
}