package linc.com.slidemenu

import android.content.Context
import android.graphics.Color
import android.util.LayoutDirection
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import linc.com.slidemenu.models.CollapseSide
import linc.com.slidemenu.models.MenuItem
import linc.com.slidemenu.models.Section
import linc.com.slidemenu.models.Shadow
import linc.com.slidemenu.util.Constants.ALPHA
import linc.com.slidemenu.util.Constants.BACKGROUND_COLOR
import linc.com.slidemenu.util.Constants.CARD_COLOR
import linc.com.slidemenu.util.Constants.CARD_ELEVATION
import linc.com.slidemenu.util.Constants.RADIUS
import linc.com.slidemenu.util.ExternalContext
import linc.com.slidemenu.util.MenuTemplate
import linc.com.slidemenu.util.MotionConnector
import java.lang.ClassCastException


open class SlideMenu private constructor(
    private val context: Context,
    private val fragment: Fragment,
    private val menuItems: List<MenuItem>,
    private val side: CollapseSide,
    private val shadow: Shadow,
    private val dragWidthPercent: Float,
    private val dragHeightPercent: Float,
    private var elevation: Float,
    private val opacity: Float,
    private val radius: Float,
    private val degreeHorizontal: Float,
    private val degreeVertical: Float,
    private val degreeAround: Float,
    private val menuTemplateTemplate: MenuTemplate?,
    // Debug params
    private val highLightDrag: Boolean

) : View.OnClickListener {

    /**
    * . . .
    * todo Task: menu controllers groups
    * . . .
    */


    // TODO: 11.11.20 navigation

    private var parentMotionLayout: MotionLayout
    private var menuHeader: RelativeLayout
    private var menuFooter: RelativeLayout
    private var menuController: LinearLayout
    private var externalContext: ExternalContext

    override fun onClick(p0: View?) {

        println("p0.id = ${p0!!.id}")
    }

    init {
        // Init external container
        externalContext = ExternalContext(context)

        // Start content fragment
        when {
            externalContext.isActivity() -> externalContext.getActivity().supportFragmentManager
            else -> externalContext.getFragment().fragmentManager
        }?.let {
            it.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.contentFragment, fragment)
                .commit()
        }

        // Init parent motion layout and menu views
         when {
            externalContext.isActivity() -> {
                parentMotionLayout = externalContext.getActivity().findViewById(R.id.motionLayout)
                menuHeader = externalContext.getActivity().findViewById(R.id.menuHeader)
                menuFooter = externalContext.getActivity().findViewById(R.id.menuFooter)
                menuController = externalContext.getActivity().findViewById(R.id.menuController)
            }
            else -> {
                parentMotionLayout = externalContext.getFragment().view!!.findViewById(R.id.motionLayout)
                menuHeader = externalContext.getActivity().findViewById(R.id.menuHeader)
                menuFooter = externalContext.getActivity().findViewById(R.id.menuFooter)
                menuController = externalContext.getActivity().findViewById(R.id.menuController)
            }
        }

        // Init MotionConnector
        MotionConnector.setParentLayout(parentMotionLayout)

        // Re-constraint layout views according to params
        rebuildLayout()

        // Apply layout customization
        applyCustomMenuParameters()

        // Add menu items to all sections
        addMenuItems()
    }

    /**
     * Layout rebuild
     * */
    private fun rebuildLayout() {
        // Rebuild layout form template
        if(menuTemplateTemplate != null) {
            rebuildLayoutFromTemplate()
            return
        }

        // TODO: 10.11.20 rebuild menu controllers view
        when(side) {
            CollapseSide.START -> {
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
            CollapseSide.END -> {
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
        applyContentCustomization(R.id.contentFragment)

        // Shadow view layout params
        applyContentCustomization(R.id.shadowMock)

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

    private fun applyContentCustomization(viewId: Int) {
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

    /**
     * Menu items
     */

    private fun addMenuItems() {
        menuItems.forEach {
            when(it.section) {
                Section.HEADER -> addHeaderItem(it)
                Section.FOOTER -> addFooterItem(it)
                Section.CONTROLLER -> addControllerItem(it)
            }
        }
    }

    private fun addHeaderItem(item: MenuItem) {
        addItemHorizontal(item, menuHeader)
    }

    private fun addFooterItem(item: MenuItem) {
        addItemHorizontal(item, menuFooter)
    }

    private fun addControllerItem(item: MenuItem) {
        menuController.addView(
            inflateResource(item),
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                gravity = when(item.horizontalPosition) {
                    MenuItem.START -> Gravity.START
                    MenuItem.CENTER -> Gravity.CENTER
                    else -> Gravity.END
                }
            }
        )
    }

    private fun addItemHorizontal(item: MenuItem, parentView: RelativeLayout) {
        parentView.addView(
            inflateResource(item),
            RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                // Align view to center
                addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)

                // Align view by horizontalPosition
                addRule(when(item.horizontalPosition) {
                    MenuItem.START -> RelativeLayout.ALIGN_PARENT_START
                    MenuItem.CENTER -> RelativeLayout.CENTER_IN_PARENT
                    else -> RelativeLayout.ALIGN_PARENT_END
                }, RelativeLayout.TRUE)
            }
        )
    }

    private fun inflateResource(item: MenuItem): View = LayoutInflater.from(context)
        .inflate(item.resource, null)
        .apply {
            // todo save id and item key to map
            id = View.generateViewId()
            setOnClickListener(this@SlideMenu)
        }


    class Builder {

        // TODO: 08.11.20 disable drag view
        // TODO: 08.11.20 add menu controller (open/close) view
        // TODO: 08.11.20 subscribe for animation progress method like listener
        // TODO: 07.11.20 handle Class errors in the future

        private lateinit var context: Context
        private lateinit var fragment: Fragment
        private val menuItems: MutableList<MenuItem> = mutableListOf()
        private lateinit var side: CollapseSide
        private var shadow: Shadow = Shadow.getDefault()
        private var dragWidthPercent: Float = 0.25f
        private var dragHeightPercent: Float = 0.4f
        private var elevation: Float = 5f
        private var opacity: Float = 1f
        private var radius: Float = 0f
        private var degreeHorizontal: Float = 0f
        private var degreeVertical: Float = 0f
        private var degreeAround: Float = 0f
        private var menuTemplateTemplate: MenuTemplate? = null
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

        fun setMenuSide(side: CollapseSide): Builder {
            this.side = side
            return this@Builder
        }

        fun addMenuItem(item: MenuItem): Builder {
            this.menuItems.add(item)
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
            this.elevation = if(elevation <= 0) 1f else elevation
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

        fun fromTemplate(menuTemplateTemplate: MenuTemplate): Builder {
            this.menuTemplateTemplate = menuTemplateTemplate
            return this@Builder
        }

        fun highlightDragView(highLightDrag: Boolean): Builder {
            this.highLightDrag = highLightDrag
            return this@Builder
        }

        fun build(): SlideMenu = SlideMenu(
            context, fragment, menuItems, side, shadow, // Main args
            dragWidthPercent, dragHeightPercent, // Drag view size
            elevation, opacity, radius, degreeHorizontal, degreeVertical, degreeAround, // Collapsed menu customization
            menuTemplateTemplate,
            highLightDrag // Debug params
        )

    }

}