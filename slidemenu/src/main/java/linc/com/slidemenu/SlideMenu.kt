package linc.com.slidemenu

import android.content.Context
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import linc.com.slidemenu.util.MotionConnector
import linc.com.slidemenu.util.Constants.ALPHA
import linc.com.slidemenu.util.Constants.RADIUS
import linc.com.slidemenu.util.Menu
import linc.com.slidemenu.util.MenuSide
import linc.com.slidemenu.util.Shadow


open class SlideMenu private constructor(
    private val context: Context,
    private val fragment: Fragment,
    private val side: MenuSide,
    private val shadow: Shadow,
    private val widthPercent: Float,  // todo Deprecated
    private val heightPercent: Float, // todo Deprecated
    private val opacity: Float,
    private val radius: Float,
    private val degreeHorizontal: Float = 0f,
    private val degreeVertical: Float = 0f,
    private val degreeAround: Float = 0f,
    private val menuTemplate: Menu? = null
) {

    // TODO: 06.11.20 footer
    // TODO: 06.11.20 header
    // TODO: 06.11.20 content
    // TODO: 06.11.20 add item to one of above parts

    /**
    * . . .
    * todo Task: apply all parameters to menu
    * . . .
    */

    init {
        // TODO: 06.11.20 rebuild scene markup according to menu side
        // TODO: 06.11.20 rebuild layout markup according to menu side

        // TODO: 07.11.20 check for class cast exceptions

        // Start content fragment
        (context as FragmentActivity).supportFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.contentFragment, fragment)
            .commit()

        rebuildLayout()
        applyCustomMenuParameters()

    }

    /**
     * Layout rebuild
     * */
    private fun rebuildLayout() {
        //
        if(menuTemplate != null) {
            rebuildLayoutFromTemplate()
            return
        }

        val contentView = (context as FragmentActivity).findViewById<MotionLayout>(R.id.motionLayout)
        MotionConnector.setParentLayout(contentView)
        when(side) {
            MenuSide.START -> {
                // Elapsed
                MotionConnector.setConstraintSet(R.id.weatherElapsed)

                // Drag view
                MotionConnector.clearConnections(R.id.dragView)
                MotionConnector.topToTopOf(R.id.dragView, ConstraintSet.PARENT_ID)
                MotionConnector.startToStartOf(R.id.dragView, ConstraintSet.PARENT_ID)

                // Collapsed
                MotionConnector.setConstraintSet(R.id.weatherCollapsed)

                // Drag view
                MotionConnector.clearConnections(R.id.dragView)
                MotionConnector.topToTopOf(R.id.dragView, ConstraintSet.PARENT_ID)
                MotionConnector.startToEndOf(R.id.dragView, ConstraintSet.PARENT_ID)
                MotionConnector.endToEndOf(R.id.dragView, ConstraintSet.PARENT_ID)

                // Content view
                MotionConnector.clearConnections(R.id.contentFragment)
                MotionConnector.topToTopOf(R.id.contentFragment, ConstraintSet.PARENT_ID)
                MotionConnector.bottomToBottomOf(R.id.contentFragment, ConstraintSet.PARENT_ID)
                MotionConnector.startToEndOf(R.id.contentFragment, ConstraintSet.PARENT_ID)
                MotionConnector.endToEndOf(R.id.contentFragment, ConstraintSet.PARENT_ID)

                // Shadow view
                MotionConnector.clearConnections(R.id.shadowMock)
                MotionConnector.topToTopOf(R.id.shadowMock, ConstraintSet.PARENT_ID)
                MotionConnector.bottomToBottomOf(R.id.shadowMock, ConstraintSet.PARENT_ID)
                MotionConnector.startToEndOf(R.id.shadowMock, ConstraintSet.PARENT_ID)
                MotionConnector.endToEndOf(R.id.shadowMock, ConstraintSet.PARENT_ID)
            }
            MenuSide.END -> {
                // Elapsed
                MotionConnector.setConstraintSet(R.id.weatherElapsed)

                // Drag view
                MotionConnector.clearConnections(R.id.dragView)
                MotionConnector.topToTopOf(R.id.dragView, ConstraintSet.PARENT_ID)
                MotionConnector.endToEndOf(R.id.dragView, ConstraintSet.PARENT_ID)

                // Collapsed
                MotionConnector.setConstraintSet(R.id.weatherCollapsed)

                // Drag view
                MotionConnector.clearConnections(R.id.dragView)
                MotionConnector.topToTopOf(R.id.dragView, ConstraintSet.PARENT_ID)
                MotionConnector.startToStartOf(R.id.dragView, ConstraintSet.PARENT_ID)
                MotionConnector.endToStartOf(R.id.dragView, ConstraintSet.PARENT_ID)

                // Content view
                MotionConnector.clearConnections(R.id.contentFragment)
                MotionConnector.topToTopOf(R.id.contentFragment, ConstraintSet.PARENT_ID)
                MotionConnector.bottomToBottomOf(R.id.contentFragment, ConstraintSet.PARENT_ID)
                MotionConnector.startToStartOf(R.id.contentFragment, ConstraintSet.PARENT_ID)
                MotionConnector.endToStartOf(R.id.contentFragment, ConstraintSet.PARENT_ID)

                // Shadow view
                MotionConnector.clearConnections(R.id.shadowMock)
                MotionConnector.topToTopOf(R.id.shadowMock, ConstraintSet.PARENT_ID)
                MotionConnector.bottomToBottomOf(R.id.shadowMock, ConstraintSet.PARENT_ID)
                MotionConnector.startToStartOf(R.id.shadowMock, ConstraintSet.PARENT_ID)
                MotionConnector.endToStartOf(R.id.shadowMock, ConstraintSet.PARENT_ID)

            }
            MenuSide.BOTTOM -> {
                println("BOTTOM")
            }
        }
    }


    private fun rebuildLayoutFromTemplate() {
        TODO("Not yet implemented")
    }

    /**
     * Scene rebuild
     * */
    private fun applyCustomMenuParameters() {
        val contentView = (context as FragmentActivity).findViewById<MotionLayout>(R.id.motionLayout)
        contentView.getConstraintSet(R.id.weatherElapsed)
            .let {
                it.setFloatValue(R.id.contentFragment, RADIUS, 0f)
                it.setFloatValue(R.id.contentFragment, ALPHA, 1f)

                // Shadow
                it.setFloatValue(R.id.shadowMock, RADIUS, 0f)
                it.setFloatValue(R.id.shadowMock, ALPHA, 0f)
            }

        contentView.getConstraintSet(R.id.weatherCollapsed)
            .let {
                it.setRotationY(R.id.contentFragment, 0f) // rotation horizontal
                it.setRotation(R.id.contentFragment, 10f) // rotate around center
                it.setFloatValue(R.id.contentFragment, RADIUS, 100f)
                it.setFloatValue(R.id.contentFragment, ALPHA, 0.2f)

                // Shadow
                it.setRotationY(R.id.shadowMock, 0f) // rotation horizontal
                it.setRotation(R.id.shadowMock, 10f) // rotate around center
                it.setFloatValue(R.id.shadowMock, RADIUS, 100f)
                it.setFloatValue(R.id.shadowMock, ALPHA, 0.5f)
            }

    }

    class Builder {

        // TODO: 08.11.20 disable drag view
        // TODO: 08.11.20 add menu controller (open/close) view
        // TODO: 08.11.20 highlight drag view
        // TODO: 08.11.20 subscribe for animation progress method like listener

        // TODO: 06.11.20 add drag view width and height percentage

        // TODO: 07.11.20 handle Class errors in the future

        private lateinit var context: Context
        private lateinit var fragment: Fragment
        private lateinit var side: MenuSide
        private var shadow: Shadow = Shadow.getDefault()
        private var widthPercent: Float = 0.4f
        private var heightPercent: Float = 0.7f
        private var opacity: Float = 1f
        private var radius: Float = 0f
        private var degreeHorizontal: Float = 0f
        private var degreeVertical: Float = 0f
        private var degreeAround: Float = 0f
        private var menuTemplate: Menu? = null


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

        fun setContentShadow(shadow: Shadow): Builder {
            this.shadow = shadow
            return this@Builder
        }

        fun setContentCollapsedWidth(widthPercent: Float): Builder {
            this.widthPercent = widthPercent
            return this@Builder
        }

        fun setContentCollapsedHeight(heightPercent: Float): Builder {
            this.heightPercent = heightPercent
            return this@Builder
        }

        fun setContentRoundedCorners(radius: Float): Builder {
            this.radius = radius
            return this@Builder
        }

        fun setContentOpacity(opacity: Float): Builder {
            this.opacity = opacity
            return this@Builder
        }

        fun rotateContentHorizontal(degreeHorizontal: Float): Builder {
            this.degreeHorizontal = degreeHorizontal
            return this@Builder
        }

        fun rotateContentVertical(degreeVertical: Float): Builder {
            this.degreeVertical = degreeVertical
            return this@Builder
        }

        fun rotateContentAround(degreeAround: Float): Builder {
            this.degreeAround = degreeAround
            return this@Builder
        }

        fun fromTemplate(menuTemplate: Menu) {
            this.menuTemplate = menuTemplate
        }

        fun build(): SlideMenu = SlideMenu(
            context, fragment, side, shadow, // Main args
            widthPercent, heightPercent,
            opacity, radius,
            degreeHorizontal, degreeVertical, degreeAround,
            menuTemplate
        )

    }

}

/*
val contentView = (context as FragmentActivity).findViewById<MotionLayout>(R.id.motionLayout)

        context.supportFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.contentFragment, fragment)
            .commit()

        contentView.getConstraintSet(R.id.weatherElapsed)
            .let {
                it.setFloatValue(R.id.contentFragment, RADIUS, 0f)
                it.setFloatValue(R.id.contentFragment, ALPHA, 1f)

                // Shadow
                it.setFloatValue(R.id.shadowMock, RADIUS, 0f)
                it.setFloatValue(R.id.shadowMock, ALPHA, 0f)
            }

        contentView.getConstraintSet(R.id.weatherCollapsed)
            .let {
                it.setRotationY(R.id.contentFragment, 0f) // rotation horizontal
                it.setRotation(R.id.contentFragment, 10f) // rotate around center
                it.setFloatValue(R.id.contentFragment, RADIUS, 100f)
                it.setFloatValue(R.id.contentFragment, ALPHA, 0.2f)

                // Shadow
                it.setRotationY(R.id.shadowMock, 0f) // rotation horizontal
                it.setRotation(R.id.shadowMock, 10f) // rotate around center
                it.setFloatValue(R.id.shadowMock, RADIUS, 100f)
                it.setFloatValue(R.id.shadowMock, ALPHA, 0.5f)
            }
 */