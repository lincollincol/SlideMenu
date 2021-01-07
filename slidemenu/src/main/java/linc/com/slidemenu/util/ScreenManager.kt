package linc.com.slidemenu.util

import android.graphics.Point
import android.view.Display


internal object ScreenManager {


    private var prev_y: Int = 0
    private var max_x: Int = 0
    private var max_y: Int = 0


    fun init() {
        val display: Display = when {
            ExternalContext.isActivity() -> {
                ExternalContext.getActivity().windowManager.defaultDisplay
            }
            else -> {
                ExternalContext.getFragment().activity!!.windowManager.defaultDisplay
            }
        }
        val size = Point()
        display.getSize(size)
        max_x = size.x
        max_y = size.y
        println(max_x)
        println(max_y)
    }

    fun currentPercentPointByX(x: Int) = (x * 100) / max_x

    fun currentPercentPointByX(x: Float) = x / max_x

    fun currentPercentPointByY(y: Int) = (y * 100) / max_y

    fun currentPercentPointByY(y: Float) = y / max_y



}