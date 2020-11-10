package linc.com.slidemenu

import android.content.Context
import android.graphics.Color
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import linc.com.slidemenu.util.MotionConnector
import linc.com.slidemenu.util.Constants.ALPHA
import linc.com.slidemenu.util.Constants.BACKGROUND_COLOR
import linc.com.slidemenu.util.Constants.CARD_COLOR
import linc.com.slidemenu.util.Constants.CARD_ELEVATION
import linc.com.slidemenu.util.Constants.RADIUS
import linc.com.slidemenu.util.Menu
import linc.com.slidemenu.util.MenuSide
import linc.com.slidemenu.util.Shadow


open class SlideMenu private constructor(
    private val context: Context,
    private val fragment: Fragment,
    private val side: MenuSide,
    private val shadow: Shadow,
    private val dragWidthPercent: Float,
    private val dragHeightPercent: Float,
    private var elevation: Float,
    private val opacity: Float,
    private val radius: Float,
    private val degreeHorizontal: Float,
    private val degreeVertical: Float,
    private val degreeAround: Float,
    private val menuTemplate: Menu?,
    // Debug params
    private var highLightDrag: Boolean

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

    private var parentMotionLayout: MotionLayout

    init {

        // TODO: 07.11.20 check for class cast exceptions

        // Start content fragment
        (context as FragmentActivity).supportFragmentManager
            .beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.contentFragment, fragment)
            .commit()

        parentMotionLayout = context.findViewById(R.id.motionLayout)

        // Init MotionConnector
        MotionConnector.setParentLayout(parentMotionLayout)

        // Apply external customization
        rebuildLayout()
        applyCustomMenuParameters()
    }

    /**
     * Layout rebuild
     * */
    private fun rebuildLayout() {
        // Rebuild layout form template
        if(menuTemplate != null) {
            rebuildLayoutFromTemplate()
            return
        }

        when(side) {
            MenuSide.START -> {
                // Elapsed
                MotionConnector.setConstraintSet(R.id.weatherElapsed)

                // Drag view
                MotionConnector.clearConnections(R.id.dragView)
                MotionConnector.topToTopOf(R.id.dragView, ConstraintSet.PARENT_ID)
                MotionConnector.startToStartOf(R.id.dragView, ConstraintSet.PARENT_ID)

                // Content and shadow
                MotionConnector.allToView(R.id.contentFragment, ConstraintSet.PARENT_ID)
                MotionConnector.allToView(R.id.shadowMock, ConstraintSet.PARENT_ID)

                // Collapsed
                MotionConnector.setConstraintSet(R.id.weatherCollapsed)

                // Content view
                MotionConnector.clearConnections(R.id.contentFragment)
                MotionConnector.startToEndOf(R.id.contentFragment, ConstraintSet.PARENT_ID)
                MotionConnector.endToEndOf(R.id.contentFragment, ConstraintSet.PARENT_ID)

                // Drag view
                MotionConnector.allToView(R.id.dragView, R.id.contentFragment)

                // Shadow view
                MotionConnector.allToView(R.id.shadowMock, R.id.contentFragment)
            }
            MenuSide.END -> {
                // Elapsed
                MotionConnector.setConstraintSet(R.id.weatherElapsed)

                // Drag view
                MotionConnector.clearConnections(R.id.dragView)
                MotionConnector.topToTopOf(R.id.dragView, ConstraintSet.PARENT_ID)
                MotionConnector.endToEndOf(R.id.dragView, ConstraintSet.PARENT_ID)

                // Content and shadow
                MotionConnector.allToView(R.id.contentFragment, ConstraintSet.PARENT_ID)
                MotionConnector.allToView(R.id.shadowMock, ConstraintSet.PARENT_ID)

                // Collapsed
                MotionConnector.setConstraintSet(R.id.weatherCollapsed)

                // Content view
                MotionConnector.clearConnections(R.id.contentFragment)
                MotionConnector.startToStartOf(R.id.contentFragment, ConstraintSet.PARENT_ID)
                MotionConnector.endToStartOf(R.id.contentFragment, ConstraintSet.PARENT_ID)

                // Drag view
                MotionConnector.allToView(R.id.dragView, R.id.contentFragment)

                // Shadow view
                MotionConnector.allToView(R.id.shadowMock, R.id.contentFragment)

            }
            MenuSide.BOTTOM -> {
                // TODO: 10.11.20 create bottom menu
            }
        }
    }


    private fun rebuildLayoutFromTemplate() {
        TODO("Not yet implemented")
    }

    /**
     * Layout customization
     */
    private fun applyCustomMenuParameters() {
        // Drag view layout params
        applyDragCustomization()

        // Content view layout params
        applyMenuCustomization(R.id.contentFragment)

        // Shadow view layout params
        applyMenuCustomization(R.id.shadowMock)

        // Shadow customization
        applyShadowCustomization()
    }


    private fun applyDragCustomization() {
        parentMotionLayout.getConstraintSet(R.id.weatherElapsed)
            .let {
                it.constrainPercentHeight(R.id.dragView, dragHeightPercent)
                it.constrainPercentWidth(R.id.dragView, dragWidthPercent)
                if(highLightDrag) {
                    it.setIntValue(R.id.dragView, BACKGROUND_COLOR, Color.RED)
                    it.setFloatValue(R.id.dragView, ALPHA, 0.3f)
                }
            }
        parentMotionLayout.getConstraintSet(R.id.weatherCollapsed)
            .let {
                if(highLightDrag) {
                    it.setIntValue(R.id.dragView, BACKGROUND_COLOR, Color.RED)
                    it.setFloatValue(R.id.dragView, ALPHA, 0.3f)
                }
            }
    }

    private fun applyMenuCustomization(viewId: Int) {
        parentMotionLayout.getConstraintSet(R.id.weatherElapsed)
            .let {
                it.setFloatValue(viewId, CARD_ELEVATION, 1f)
            }
        parentMotionLayout.getConstraintSet(R.id.weatherCollapsed)
            .let {
                it.setRotationY(viewId, degreeHorizontal)
                it.setRotationX(viewId, degreeVertical)
                it.setRotation(viewId, degreeAround) // rotate around center
                it.setFloatValue(viewId, RADIUS, radius)
                it.setFloatValue(viewId, ALPHA, opacity)
                it.setFloatValue(viewId, CARD_ELEVATION, elevation)
            }
    }

    private fun applyShadowCustomization() {
        parentMotionLayout.getConstraintSet(R.id.weatherElapsed)
            .let {
                it.setFloatValue(R.id.shadowMock, ALPHA, 0f)
                it.setIntValue(R.id.shadowMock, CARD_COLOR, shadow.color)
            }
        parentMotionLayout.getConstraintSet(R.id.weatherCollapsed)
            .let {
                it.setFloatValue(R.id.shadowMock, ALPHA, shadow.opacity)
                it.setIntValue(R.id.shadowMock, CARD_COLOR, shadow.color)
            }
    }

    class Builder {

        // TODO: 08.11.20 disable drag view
        // TODO: 08.11.20 add menu controller (open/close) view
        // TODO: 08.11.20 subscribe for animation progress method like listener
        // TODO: 07.11.20 handle Class errors in the future

        private lateinit var context: Context
        private lateinit var fragment: Fragment
        private lateinit var side: MenuSide
        private var shadow: Shadow = Shadow.getDefault()
        private var dragWidthPercent: Float = 0.25f
        private var dragHeightPercent: Float = 0.4f
        private var elevation: Float = 10f
        private var opacity: Float = 1f
        private var radius: Float = 0f
        private var degreeHorizontal: Float = 0f
        private var degreeVertical: Float = 0f
        private var degreeAround: Float = 0f
        private var menuTemplate: Menu? = null
        // TODO: 10.11.20 separate values to constants
        /**
         * Debug params
         */
        private var highLightDrag: Boolean = false



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

        fun setDragViewWidth(dragWidthPercent: Float): Builder {
            this.dragWidthPercent = dragWidthPercent
            return this@Builder
        }

        fun setDragViewHeight(dragHeightPercent: Float): Builder {
            this.dragHeightPercent = dragHeightPercent
            return this@Builder
        }

        fun setContentRoundedCorners(radius: Float): Builder {
            this.radius = radius
            return this@Builder
        }

        fun setContentElevation(elevation: Float): Builder {
            this.elevation = elevation
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

        fun fromTemplate(menuTemplate: Menu): Builder {
            this.menuTemplate = menuTemplate
            return this@Builder
        }

        fun highlightDragView(highLightDrag: Boolean): Builder {
            this.highLightDrag = highLightDrag
            return this@Builder
        }

        fun build(): SlideMenu = SlideMenu(
            context, fragment, side, shadow, // Main args
            dragWidthPercent, dragHeightPercent, // Drag view size
            elevation, opacity, radius, degreeHorizontal, degreeVertical, degreeAround, // Collapsed menu customization
            menuTemplate,
            highLightDrag // Debug params
        )

    }

}