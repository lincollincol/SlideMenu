package linc.com.slidemenu.util

import android.graphics.Color
import androidx.annotation.ColorInt

data class Shadow(
    @ColorInt internal val color: Int,
    internal val opacity: Float
) {
    companion object {
        fun getDefault() = Shadow(Color.WHITE, 0f)
    }
}