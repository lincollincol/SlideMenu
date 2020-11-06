package linc.com.slidemenu

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.slide_menu_layout.view.*
import linc.com.slidemenu.util.MenuSide
import linc.com.weatherapp.utils.ScreenSizeUtil


open class SlideMenu private constructor(
    private val fragment: Fragment,
    private val side: MenuSide,
    private val context: Context,

    private val widthPercent: Float,
    private val heightPercent: Float,
    private val shadow: Float,
    private val opacity: Float,
    private val corners: Int
) {

    // TODO: 06.11.20 footer
    // TODO: 06.11.20 header
    // TODO: 06.11.20 content
    // TODO: 06.11.20 add item to one of above parts

    private var screenWidth: Int = 0

    init {
        // TODO: 06.11.20 set scene according to menu side
        // TODO: 06.11.20 set guide lines according to menu side

        val contentView = (context as FragmentActivity).findViewById<MotionLayout>(R.id.motionLayout)
        // Init and calculate size
        ScreenSizeUtil.init(context)
        ScreenSizeUtil.calculateSize()

        (context as FragmentActivity).supportFragmentManager
            .beginTransaction()
            .replace(R.id.contentFragment, fragment)
            .commit()



    }

    class Builder {
        // TODO: 06.11.20 add drag view width and height percentage
        // TODO: 06.11.20 rotate percentage
        private lateinit var fragment: Fragment
        private lateinit var side: MenuSide
        private lateinit var context: Context
        private var widthPercent: Float = 0.4f
        private var heightPercent: Float = 0.7f
        private var shadow: Float = 0f
        private var opacity: Float = 1f
        private var corners: Int = 0

        fun withContext(context: Context): Builder {
            this.context = context
            return this@Builder
        }

        fun setContentFragment(fragment: Fragment): Builder {
            this.fragment = fragment
            return this@Builder
        }

        fun setMenuSide(side: MenuSide): Builder {
            this.side = side
            return this@Builder
        }

        fun setContentCollapsedWidth(widthPercent: Float): Builder {
            this.widthPercent = widthPercent
            return this@Builder
        }

        fun setContentCollapsedHeight(heightPercent: Float): Builder {
            this.heightPercent = heightPercent
            // res = 100 - heightPercent
            // oneSide = res / 2
            // set bottom and tom to one side
            return this@Builder
        }

        fun setContentRoundedCorners(corners: Int): Builder {
            this.corners = corners
            return this@Builder
        }

        fun setContentOpacity(opacity: Float): Builder {
            this.opacity = opacity
            return this@Builder
        }

        fun setContentShadowOpacity(shadow: Float): Builder {
            this.shadow = shadow
            return this@Builder
        }

        fun build(): SlideMenu = SlideMenu(
            fragment, side, context, widthPercent, heightPercent, shadow, opacity, corners
        )

    }

}