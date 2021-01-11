package linc.com.slidemenu.models

import android.graphics.Color
import androidx.annotation.ColorInt

data class Shadow(
    @ColorInt internal val color: Int,
    internal val opacity: Float
) {
    internal companion object {
        fun getDefault() = Shadow(Color.WHITE, 0f)
    }
}