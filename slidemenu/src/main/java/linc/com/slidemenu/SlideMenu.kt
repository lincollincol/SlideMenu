package linc.com.slidemenu

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Point
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.children
import androidx.fragment.app.Fragment
import linc.com.slidemenu.models.CollapseSide
import linc.com.slidemenu.models.MenuItem
import linc.com.slidemenu.models.Section
import linc.com.slidemenu.models.Shadow
import linc.com.slidemenu.util.Constants.ALPHA
import linc.com.slidemenu.util.Constants.BACKGROUND_COLOR
import linc.com.slidemenu.util.Constants.CARD_COLOR
import linc.com.slidemenu.util.Constants.CARD_ELEVATION
import linc.com.slidemenu.util.Constants.LANDSCAPE_CONTROLLER_FULL_HEIGHT_PERCENT
import linc.com.slidemenu.util.Constants.LANDSCAPE_CONTROLLER_ONE_GROUP_HEIGHT_PERCENT
import linc.com.slidemenu.util.Constants.LANDSCAPE_CONTROLLER_TWO_GROUPS_HEIGHT_PERCENT
import linc.com.slidemenu.util.Constants.LANDSCAPE_HEADER_HEIGHT_PERCENT
import linc.com.slidemenu.util.Constants.RADIUS
import linc.com.slidemenu.util.ExternalContext
import linc.com.slidemenu.util.MenuTemplate
import linc.com.slidemenu.util.MotionConnector
import linc.com.slidemenu.util.ScreenManager


open class SlideMenu private constructor(
    private val context: Context,
    private val fragment: Fragment,
    private val menuItems: List<MenuItem>,
    private val controllerGravity: Int,
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
    private val highlightDrag: Boolean

) : View.OnClickListener {

    /**
    * . . .
    * todo Task: menu click listener
    * todo Task: landscape rotation
    * . . .
    */

    // TODO: 11.11.20 navigation
    // TODO: 11.11.20 click listener

    private val parentMotionLayout: MotionLayout
    private val menuHeader: RelativeLayout
    private val menuFooter: RelativeLayout
    private val menuController: LinearLayout

    private var hasHeader = false
    private var hasFooter = false

    private val handler = Handler()

    override fun onClick(p0: View?) {

        println("p0.id = ${p0!!.id}")
    }

    init {
        // Init external container
        ExternalContext.init(context)
        ScreenManager.init()

        // Start content fragment
        when {
            ExternalContext.isActivity() -> ExternalContext.getActivity().supportFragmentManager
            else -> ExternalContext.getFragment().fragmentManager
        }?.let {
            it.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.contentFragment, fragment)
                .commit()
        }

        // Init parent motion layout and menu views

        parentMotionLayout = ExternalContext.findViewById(R.id.motionLayout)
        menuHeader = ExternalContext.findViewById(R.id.menuHeader)
        menuFooter = ExternalContext.findViewById(R.id.menuFooter)
        menuController = ExternalContext.findViewById(R.id.menuController)

        // Init MotionConnector
        MotionConnector.setParentLayout(parentMotionLayout)

        // Re-constraint layout views according to params
        rebuildLayout()

        // Apply layout customization
        applyCustomMenuParameters()

        // Add menu items to all sections
        addMenuItems()

        // Handle menu items clicks according to menu state
        enableMenuItemsClicks()
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

                // Menu controllers
                MotionConnector.removeConnection(R.id.menuControllersScrollView, ConstraintSet.END)
                MotionConnector.startToStartOf(
                    R.id.menuControllersScrollView,
                    ConstraintSet.PARENT_ID
                )

/*
                // Menu header
                MotionConnector.removeConnection(R.id.menuHeader, ConstraintSet.END)
                MotionConnector.startToStartOf(R.id.menuHeader, ConstraintSet.PARENT_ID)

                // Menu footer
                MotionConnector.removeConnection(R.id.menuFooter, ConstraintSet.END)
                MotionConnector.startToStartOf(R.id.menuFooter, ConstraintSet.PARENT_ID)
*/

                //
                // Collapsed
                //
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

                // Menu controllers
                MotionConnector.removeConnection(
                    R.id.menuControllersScrollView,
                    ConstraintSet.START
                )
                MotionConnector.endToEndOf(R.id.menuControllersScrollView, ConstraintSet.PARENT_ID)

/*
                // Menu header
                MotionConnector.removeConnection(R.id.menuHeader, ConstraintSet.START)
                MotionConnector.endToEndOf(R.id.menuHeader, ConstraintSet.PARENT_ID)

                // Menu footer
                MotionConnector.removeConnection(R.id.menuFooter, ConstraintSet.START)
                MotionConnector.endToEndOf(R.id.menuFooter, ConstraintSet.PARENT_ID)
*/

                //
                // Collapsed
                //
                MotionConnector.setConstraintSet(R.id.weatherCollapsed)

                // Content view
                MotionConnector.clearConnections(R.id.contentFragment)
                MotionConnector.allToView(R.id.contentFragment, ConstraintSet.PARENT_ID)
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
     * Screen configuration
     */
    fun handleConfiguration(newConfiguration: Configuration) {
        when(newConfiguration.orientation) {
            // Resize content fragment to landscape mode
            Configuration.ORIENTATION_LANDSCAPE -> {

                // Set 60% width under main content view to prevent progress animation
                parentMotionLayout.getConstraintSet(R.id.weatherElapsed)
                    .let {
                        it.constrainPercentWidth(R.id.menuHeader, 0.6f)
                        it.constrainPercentWidth(R.id.menuFooter, 0.6f)
                    }

                parentMotionLayout.getConstraintSet(R.id.weatherCollapsed)
                    .let {
                        it.constrainPercentWidth(
                            R.id.contentFragment,
                            0.8f
                        ) // Content fragment use 40% of screen width
                        it.constrainPercentHeight(R.id.contentFragment, 0.9f)
                        // Content fragment
                        it.setScaleX(R.id.contentFragment, 1f)
                        it.setScaleY(R.id.contentFragment, 1f)

                        // Footer and header height
                        it.constrainPercentHeight(
                            R.id.menuHeader,
                            if (hasHeader) LANDSCAPE_HEADER_HEIGHT_PERCENT else 0f
                        )
                        it.constrainPercentHeight(
                            R.id.menuFooter,
                            if (hasFooter) LANDSCAPE_HEADER_HEIGHT_PERCENT else 0f
                        )

                        // Controller group. Use height according to footer and header
                        it.constrainPercentHeight(
                            R.id.menuControllersScrollView, when {
                                hasHeader && hasFooter -> LANDSCAPE_CONTROLLER_TWO_GROUPS_HEIGHT_PERCENT
                                hasHeader || hasFooter -> LANDSCAPE_CONTROLLER_ONE_GROUP_HEIGHT_PERCENT
                                else -> LANDSCAPE_CONTROLLER_FULL_HEIGHT_PERCENT
                            }
                        )

                        // Footer and header width
                        it.constrainPercentWidth(R.id.menuHeader, 0.6f)
                        it.constrainPercentWidth(R.id.menuFooter, 0.6f)
                        it.constrainPercentWidth(R.id.menuControllersScrollView, 0.6f)

                    }

                // Disconnect footer and header to content fragment container
                fun removeMenuGroupsConnection(constraintId: Int, side: Int) {
                    MotionConnector.setConstraintSet(constraintId)
                    MotionConnector.removeConnection(R.id.menuHeader, side)
                    MotionConnector.removeConnection(R.id.menuFooter, side)
                }

                when (side) {
                    CollapseSide.START -> {
                        // Menu groups
                        removeMenuGroupsConnection(R.id.weatherCollapsed, ConstraintSet.END)
                        removeMenuGroupsConnection(R.id.weatherElapsed, ConstraintSet.END)
                    }
                    CollapseSide.END -> {
                        // Menu groups
                        removeMenuGroupsConnection(R.id.weatherCollapsed, ConstraintSet.START)
                        removeMenuGroupsConnection(R.id.weatherElapsed, ConstraintSet.START)
                    }
                }
            }
            // Set default portrait params to menu
            Configuration.ORIENTATION_PORTRAIT -> {

                // Set 100% width under main content view to prevent progress animation
                parentMotionLayout.getConstraintSet(R.id.weatherElapsed)
                    .let {
                        it.constrainPercentWidth(R.id.menuHeader, 1f)
                        it.constrainPercentWidth(R.id.menuFooter, 1f)
                    }

                // Resize content fragment to default portrait size
                parentMotionLayout.getConstraintSet(R.id.weatherCollapsed)
                    .let {
                        it.constrainPercentWidth(R.id.menuHeader, 1f)
                        it.constrainPercentWidth(R.id.menuFooter, 1f)

                        it.setScaleX(R.id.contentFragment, 0.7f)
                        it.setScaleY(R.id.contentFragment, 0.7f)
                    }

                // Connect footer and header back to parent
                fun connectMenuGroupsToParentSide(constraintId: Int, side: Int) {
                    MotionConnector.setConstraintSet(constraintId)
                    when (side) {
                        ConstraintSet.START -> {
                            MotionConnector.startToStartOf(R.id.menuHeader, ConstraintSet.PARENT_ID)
                            MotionConnector.startToStartOf(R.id.menuFooter, ConstraintSet.PARENT_ID)
                        }
                        ConstraintSet.END -> {
                            MotionConnector.endToEndOf(R.id.menuHeader, ConstraintSet.PARENT_ID)
                            MotionConnector.endToEndOf(R.id.menuFooter, ConstraintSet.PARENT_ID)
                        }
                    }
                }

                when (side) {
                    CollapseSide.START -> {
                        // Menu groups
                        connectMenuGroupsToParentSide(R.id.weatherElapsed, ConstraintSet.END)
                        connectMenuGroupsToParentSide(R.id.weatherCollapsed, ConstraintSet.END)
                    }
                    CollapseSide.END -> {
                        // Menu groups
                        connectMenuGroupsToParentSide(R.id.weatherElapsed, ConstraintSet.START)
                        connectMenuGroupsToParentSide(R.id.weatherCollapsed, ConstraintSet.START)
                    }
                }
            }
        }
    }


    /**
     * Layout customization
     */
    private fun applyCustomMenuParameters() {
        // Controller align
        menuController.gravity = controllerGravity

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
                if(highlightDrag) {
                    it.setIntValue(R.id.dragView, BACKGROUND_COLOR, Color.RED)
                    it.setFloatValue(R.id.dragView, ALPHA, 0.3f)
                }
            }
        parentMotionLayout.getConstraintSet(R.id.weatherCollapsed)
            .let {
                if(highlightDrag) {
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
        hasHeader = true
    }

    private fun addFooterItem(item: MenuItem) {
        addItemHorizontal(item, menuFooter)
        hasFooter = true
    }

    private fun addControllerItem(item: MenuItem) {
        menuController.addView(
            inflateResource(item),
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                gravity = when (item.horizontalPosition) {
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
                addRule(
                    when (item.horizontalPosition) {
                        MenuItem.START -> RelativeLayout.ALIGN_PARENT_START
                        MenuItem.CENTER -> RelativeLayout.CENTER_IN_PARENT
                        else -> RelativeLayout.ALIGN_PARENT_END
                    }, RelativeLayout.TRUE
                )
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

    private fun enableMenuItemsClicks() {

        // Disable menu items clicks
        fun enableViews(enable: Boolean) {
            menuHeader.children.forEach { it.isEnabled = enable }
            menuFooter.children.forEach { it.isEnabled = enable }
            menuController.children.forEach { it.isEnabled = enable }
        }

        // Disable for first time
        enableViews(false)

        // Disable clicks according to progress
        parentMotionLayout.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                enableViews(p0!!.progress == 1f)
            }
        })
    }

    class Builder {

        // TODO: 08.11.20 disable drag view
        // TODO: 08.11.20 add menu controller (open/close) view
        // TODO: 08.11.20 subscribe for animation progress method like listener
        // TODO: 07.11.20 handle Class errors in the future

        private lateinit var context: Context
        private lateinit var fragment: Fragment
        // Menu parameters
        private val menuItems: MutableList<MenuItem> = mutableListOf()
        private var controllerGravity: Int = Gravity.START
        // Content view parameters
        private lateinit var side: CollapseSide
        private var shadow: Shadow = Shadow.getDefault()
        private var dragWidthPercent: Float = 0.03f
        private var dragHeightPercent: Float = 0.4f
        private var elevation: Float = 5f
        private var opacity: Float = 1f
        private var radius: Float = 0f
        private var degreeHorizontal: Float = 0f
        private var degreeVertical: Float = 0f
        private var degreeAround: Float = 0f
        private var menuTemplateTemplate: MenuTemplate? = null
        // TODO: 10.11.20 separate values to constants
        // Debug params
        private var highlightDrag: Boolean = false

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

        fun setControllerSectionGravity(controllerGravity: Int): Builder {
            this.controllerGravity = controllerGravity
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
            this.highlightDrag = highLightDrag
            return this@Builder
        }

        fun build(): SlideMenu = SlideMenu(
            context,
            fragment,
            menuItems,
            controllerGravity,
            side,
            shadow, // Main args
            dragWidthPercent,
            dragHeightPercent, // Drag view size
            elevation,
            opacity,
            radius,
            degreeHorizontal,
            degreeVertical,
            degreeAround, // Collapsed menu customization
            menuTemplateTemplate,
            highlightDrag // Debug params
        )

    }
    
    interface ClickListener {
        fun onItemClicked(item: Map<String, Int>)
    }

}



/*



<?xml version="1.0" encoding="utf-8"?>
<MotionScene xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <Transition
        app:constraintSetStart="@id/weatherElapsed"
        app:constraintSetEnd="@id/weatherCollapsed"
        app:duration="300">

        <OnSwipe
            app:onTouchUp="autoComplete"
            app:dragDirection="dragStart"
            app:touchRegionId="@id/dragView"
            app:touchAnchorId="@id/dragView"/>

    </Transition>

    <ConstraintSet
        android:id="@+id/weatherElapsed">

        <!-- Content declaration -->

        <!--            android:elevation="10dp"-->
        <Constraint
            android:id="@+id/dragView"
            android:elevation="2dp"
            app:visibilityMode="ignore" />

        <Constraint
            android:id="@+id/contentFragment">
            <Layout
                android:layout_width="0dp"
                android:layout_height="0dp" />
            <Transform
                android:scaleX="1"
                android:scaleY="1"
                android:elevation="1dp" />
        </Constraint>

        <Constraint
            android:id="@+id/shadowMock">
            <Layout
                android:layout_width="0dp"
                android:layout_height="0dp"
                android:elevation="1dp" />
            <Transform
                android:scaleX="1"
                android:scaleY="1"/>
        </Constraint>

        <!-- Menu declaration -->
        <Constraint
            android:id="@+id/menuHeader">
            <Layout
                app:layout_constraintWidth_percent="1"
                app:layout_constraintHeight_percent="0.15"
                app:layout_constraintTop_toTopOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintEnd_toEndOf="parent"/>
            <PropertySet
                android:alpha="0.5"/>
        </Constraint>

        <Constraint
            android:id="@+id/menuControllersScrollView">
            <Layout
                app:layout_constraintWidth_percent="0.65"
                app:layout_constraintHeight_percent="0.7"
                app:layout_constraintTop_toBottomOf="@id/menuHeader"
                app:layout_constraintBottom_toTopOf="@id/menuFooter"
                app:layout_constraintStart_toStartOf="parent"/>
            <PropertySet
                android:alpha="0.5"/>
        </Constraint>

        <Constraint
            android:id="@+id/menuFooter">
            <Layout
                app:layout_constraintWidth_percent="1"
                app:layout_constraintHeight_percent="0.15"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintEnd_toEndOf="parent"/>
            <PropertySet
                android:alpha="0.5"/>
        </Constraint>

    </ConstraintSet>

    <ConstraintSet
        android:id="@+id/weatherCollapsed"
        app:deriveConstraintsFrom="@+id/weatherElapsed">

        <!-- Content declaration -->
        <Constraint
            android:id="@+id/dragView"
            app:visibilityMode="ignore" >
            <Layout
                app:layout_constraintWidth_percent="0.7"
                app:layout_constraintHeight_percent="0.7" />
        </Constraint>

        <Constraint
            android:id="@+id/contentFragment">
            <Layout
                app:layout_constraintWidth_percent="0.99"
                app:layout_constraintHeight_percent="0.99"/>
            <Transform
                android:scaleX="0.7"
                android:scaleY="0.7"
                android:elevation="1dp" />
        </Constraint>

        <Constraint
            android:id="@+id/shadowMock">
            <Layout
                app:layout_constraintWidth_percent="0.99"
                app:layout_constraintHeight_percent="0.99"/>
            <Transform
                android:scaleX="0.7"
                android:scaleY="0.7"
                android:elevation="1dp" />
        </Constraint>

        <!-- Menu declaration -->

        <Constraint
            android:id="@+id/menuHeader">
            <PropertySet
                android:alpha="1"/>
        </Constraint>

        <Constraint
            android:id="@+id/menuControllersScrollView">
            <PropertySet
                android:alpha="1"/>
        </Constraint>

        <Constraint
            android:id="@+id/menuFooter">
            <PropertySet
                android:alpha="1"/>
        </Constraint>

    </ConstraintSet>

</MotionScene>




 */