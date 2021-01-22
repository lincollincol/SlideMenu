package linc.com.slidemenu

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.os.Handler
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.children
import androidx.fragment.app.Fragment
import linc.com.slidemenu.models.CollapseSide
import linc.com.slidemenu.models.MenuItem
import linc.com.slidemenu.models.Section
import linc.com.slidemenu.models.Shadow
import linc.com.slidemenu.util.*
import linc.com.slidemenu.util.Constants
import linc.com.slidemenu.util.ExternalContext
import linc.com.slidemenu.util.MotionConnector
import linc.com.slidemenu.util.ScreenManager


class SlideMenuLayout(
    context: Context,
    attrs: AttributeSet
) : MotionLayout(context, attrs), View.OnClickListener {

    // Menu parameters
    private val menuItems: MutableList<MenuItem> = mutableListOf()
    var controllerGravity: Int = Gravity.START
    // Content view parameters
    private var dragWidthPercent: Float = 0.03f
    private var dragHeightPercent: Float = 0.4f

    var side: CollapseSide
    var shadow: Shadow
    var containerElevation: Float
    var opacity: Float
    var radius: Float
    var degreeHorizontal: Float
    var degreeVertical: Float
    var degreeAround: Float

    private var menuTemplateTemplate: MenuTemplate? = null
    // TODO: 10.11.20 separate values to constants
    // Debug params
    var highlightDrag: Boolean = false

    private lateinit var dragView: View
    private lateinit var fragmentContainer: SlideContainerView
    private lateinit var shadowMock: CardView
    private lateinit var menuHeader: RelativeLayout
    private lateinit var menuFooter: RelativeLayout
    private lateinit var menuController: LinearLayout
    private lateinit var menuControllerScrollView: ScrollView

    private var hasHeader = false
    private var hasFooter = false

    init {

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.SlideMenuLayout)

        // TODO: 20.01.21 add attributes validation
        highlightDrag = attributes.getBoolean(R.styleable.SlideMenuLayout_highlightDrag, false)
        shadow = Shadow(attributes.getColor(R.styleable.SlideMenuLayout_shadowColor, Color.TRANSPARENT), 1f)
        side = when (attributes.getInt(R.styleable.SlideMenuLayout_collapseSide, -1)) {
            1 -> CollapseSide.START
            else -> CollapseSide.END
        }

        containerElevation = attributes.getFloat(R.styleable.SlideMenuLayout_collapseElevation, 5f)
        opacity = attributes.getFloat(R.styleable.SlideMenuLayout_collapseOpacity, 1f)
        radius = attributes.getFloat(R.styleable.SlideMenuLayout_collapseCornersRadius, 0f)
        degreeHorizontal = attributes.getFloat(R.styleable.SlideMenuLayout_rotationHorizontalDegree, 0f)
        degreeVertical = attributes.getFloat(R.styleable.SlideMenuLayout_rotationVerticalDegree, 0f)
        degreeAround = attributes.getFloat(R.styleable.SlideMenuLayout_rotationAroundDegree, 0f)


        attributes.recycle()

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        var hasFragmentContainer = false

        for (index in 0 until childCount) {
            hasFragmentContainer = getChildAt(index) is SlideContainerView
            if(hasFragmentContainer){
                fragmentContainer = getChildAt(index) as SlideContainerView
                break
            }
        }

        if(!hasFragmentContainer)
            throw ContainerNotFoundException()

        if(fragmentContainer.id != R.id.slideFragmentContainer)
            throw InvalidIdException()

        // Prepare menu views
        inflateMenu()
    }

    fun refresh() {
        // Re-constraint layout views according to params
        rebuildLayout()

        // Apply layout customization
        applyCustomMenuParameters()
    }

    private fun inflateMenu() {
        // TODO: 19.01.21 separate to new fun like "init views"
        // Init parent motion layout and menu views
        dragView = LayoutInflater.from(context).inflate(R.layout.default_slide_drag, null)
        shadowMock = LayoutInflater.from(context).inflate(R.layout.default_slide_shadow, null) as CardView
        menuHeader = LayoutInflater.from(context).inflate(R.layout.default_slide_header, null) as RelativeLayout
        menuFooter = LayoutInflater.from(context).inflate(R.layout.default_slide_footer, null) as RelativeLayout
        menuControllerScrollView = LayoutInflater.from(context).inflate(R.layout.default_slide_menu, null) as ScrollView
        menuController = menuControllerScrollView.findViewById(R.id.menuController)

        dragView.requestLayout()
        shadowMock.requestLayout()
        menuHeader.requestLayout()
        menuFooter.requestLayout()
        menuControllerScrollView.requestLayout()
        menuController.requestLayout()

        this@SlideMenuLayout.addView(dragView)
        this@SlideMenuLayout.addView(shadowMock)
        this@SlideMenuLayout.addView(menuControllerScrollView)
        this@SlideMenuLayout.addView(menuHeader)
        this@SlideMenuLayout.addView(menuFooter)



        // Init MotionConnector
        MotionConnector.setParentLayout(this@SlideMenuLayout)

        // Re-constraint layout views according to params
        rebuildLayout()

        // Apply layout customization
        applyCustomMenuParameters()

        // Add menu items to all sections
        addMenuItems()

        // Handle menu items clicks according to menu state
        enableMenuItemsClicks()
    }


    override fun onClick(p0: View?) {

        println("p0.id = ${p0!!.id}")
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
                MotionConnector.clearConnections(dragView.id)
                MotionConnector.topToTopOf(dragView.id, ConstraintSet.PARENT_ID)
                MotionConnector.startToStartOf(dragView.id, ConstraintSet.PARENT_ID)

                // Content and shadow
                MotionConnector.allToView(fragmentContainer.id, ConstraintSet.PARENT_ID)
                MotionConnector.allToView(shadowMock.id, ConstraintSet.PARENT_ID)

                // Menu controllers
                MotionConnector.removeConnection(menuControllerScrollView.id, ConstraintSet.END)
                MotionConnector.startToStartOf(menuControllerScrollView.id, ConstraintSet.PARENT_ID)

                //
                // Collapsed
                //
                MotionConnector.setConstraintSet(R.id.weatherCollapsed)

                // Content view
                MotionConnector.clearConnections(fragmentContainer.id)
                MotionConnector.startToEndOf(fragmentContainer.id, ConstraintSet.PARENT_ID)
                MotionConnector.endToEndOf(fragmentContainer.id, ConstraintSet.PARENT_ID)

                // Drag view
                MotionConnector.allToView(dragView.id, fragmentContainer.id)

                // Shadow view
                MotionConnector.allToView(shadowMock.id, fragmentContainer.id)

            }
            CollapseSide.END -> {
                // Elapsed
                MotionConnector.setConstraintSet(R.id.weatherElapsed)

                // Drag view
                MotionConnector.clearConnections(dragView.id)
                MotionConnector.topToTopOf(dragView.id, ConstraintSet.PARENT_ID)
                MotionConnector.endToEndOf(dragView.id, ConstraintSet.PARENT_ID)

                // Content and shadow
                MotionConnector.allToView(fragmentContainer.id, ConstraintSet.PARENT_ID)
                MotionConnector.allToView(shadowMock.id, ConstraintSet.PARENT_ID)

                // Menu controllers
                MotionConnector.removeConnection(
                    menuControllerScrollView.id,
                    ConstraintSet.START
                )
                MotionConnector.endToEndOf(menuControllerScrollView.id, ConstraintSet.PARENT_ID)

                //
                // Collapsed
                //
                MotionConnector.setConstraintSet(R.id.weatherCollapsed)

                // Content view
                MotionConnector.clearConnections(fragmentContainer.id)
                MotionConnector.allToView(fragmentContainer.id, ConstraintSet.PARENT_ID)
                MotionConnector.startToStartOf(fragmentContainer.id, ConstraintSet.PARENT_ID)
                MotionConnector.endToStartOf(fragmentContainer.id, ConstraintSet.PARENT_ID)

                // Drag view
                MotionConnector.allToView(dragView.id, fragmentContainer.id)

                // Shadow view
                MotionConnector.allToView(shadowMock.id, fragmentContainer.id)

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
                this@SlideMenuLayout.getConstraintSet(R.id.weatherElapsed)
                    .let {
                        it.constrainPercentWidth(menuHeader.id, 0.6f)
                        it.constrainPercentWidth(menuFooter.id, 0.6f)
                    }

                this@SlideMenuLayout.getConstraintSet(R.id.weatherCollapsed)
                    .let {
                        it.constrainPercentWidth(
                            fragmentContainer.id,
                            0.8f
                        ) // Content fragment use 40% of screen width
                        it.constrainPercentHeight(fragmentContainer.id, 0.9f)
                        // Content fragment
                        it.setScaleX(fragmentContainer.id, 1f)
                        it.setScaleY(fragmentContainer.id, 1f)

                        // Footer and header height
                        it.constrainPercentHeight(
                            menuHeader.id,
                            if (hasHeader) Constants.LANDSCAPE_HEADER_HEIGHT_PERCENT else 0f
                        )
                        it.constrainPercentHeight(
                            menuFooter.id,
                            if (hasFooter) Constants.LANDSCAPE_HEADER_HEIGHT_PERCENT else 0f
                        )

                        // Controller group. Use height according to footer and header
                        it.constrainPercentHeight(
                            menuControllerScrollView.id, when {
                                hasHeader && hasFooter -> Constants.LANDSCAPE_CONTROLLER_TWO_GROUPS_HEIGHT_PERCENT
                                hasHeader || hasFooter -> Constants.LANDSCAPE_CONTROLLER_ONE_GROUP_HEIGHT_PERCENT
                                else -> Constants.LANDSCAPE_CONTROLLER_FULL_HEIGHT_PERCENT
                            }
                        )

                        // Footer and header width
                        it.constrainPercentWidth(menuHeader.id, 0.6f)
                        it.constrainPercentWidth(menuFooter.id, 0.6f)
                        it.constrainPercentWidth(menuControllerScrollView.id, 0.6f)

                    }

                // Disconnect footer and header to content fragment container
                fun removeMenuGroupsConnection(constraintId: Int, side: Int) {
                    MotionConnector.setConstraintSet(constraintId)
                    MotionConnector.removeConnection(menuHeader.id, side)
                    MotionConnector.removeConnection(menuFooter.id, side)
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
                this@SlideMenuLayout.getConstraintSet(R.id.weatherElapsed)
                    .let {
                        it.constrainPercentWidth(menuHeader.id, 1f)
                        it.constrainPercentWidth(menuFooter.id, 1f)
                    }

                // Resize content fragment to default portrait size
                this@SlideMenuLayout.getConstraintSet(R.id.weatherCollapsed)
                    .let {
                        it.constrainPercentWidth(menuHeader.id, 1f)
                        it.constrainPercentWidth(menuFooter.id, 1f)

                        it.setScaleX(fragmentContainer.id, 0.7f)
                        it.setScaleY(fragmentContainer.id, 0.7f)
                    }

                // Connect footer and header back to parent
                fun connectMenuGroupsToParentSide(constraintId: Int, side: Int) {
                    MotionConnector.setConstraintSet(constraintId)
                    when (side) {
                        ConstraintSet.START -> {
                            MotionConnector.startToStartOf(menuHeader.id, ConstraintSet.PARENT_ID)
                            MotionConnector.startToStartOf(menuFooter.id, ConstraintSet.PARENT_ID)
                        }
                        ConstraintSet.END -> {
                            MotionConnector.endToEndOf(menuHeader.id, ConstraintSet.PARENT_ID)
                            MotionConnector.endToEndOf(menuFooter.id, ConstraintSet.PARENT_ID)
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
        applyContentCustomization(fragmentContainer.id)

        // Shadow view layout params
        applyContentCustomization(shadowMock.id)

        // Shadow customization
        applyShadowCustomization()
    }

    private fun applyDragCustomization() {
        this@SlideMenuLayout.getConstraintSet(R.id.weatherElapsed)
            .let {
                it.constrainPercentHeight(dragView.id, dragHeightPercent)
                it.constrainPercentWidth(dragView.id, dragWidthPercent)
                if(highlightDrag) {
                    it.setIntValue(dragView.id, Attribute.BACKGROUND_COLOR, Color.RED)
                    it.setFloatValue(dragView.id, Attribute.ALPHA, 0.3f)
                }
            }
        this@SlideMenuLayout.getConstraintSet(R.id.weatherCollapsed)
            .let {
                it.setRotationY(dragView.id, degreeHorizontal)
                it.setRotationX(dragView.id, degreeVertical)
                it.setRotation(dragView.id, 45f)

                println(degreeHorizontal)
                println(degreeVertical)
                println(degreeAround)


                if(highlightDrag) {
                    it.setIntValue(dragView.id, Attribute.BACKGROUND_COLOR, Color.RED)
                    it.setFloatValue(dragView.id, Attribute.ALPHA, 0.3f)
                }
            }
    }

    private fun applyContentCustomization(viewId: Int) {
        this@SlideMenuLayout.getConstraintSet(R.id.weatherElapsed)
            .let {
                it.setFloatValue(viewId, Attribute.CARD_ELEVATION, 1f)
                it.setFloatValue(viewId, Attribute.RADIUS, 0f)
                it.setFloatValue(viewId, Attribute.ALPHA, 1f)
            }
        this@SlideMenuLayout.getConstraintSet(R.id.weatherCollapsed)
            .let {
                it.setRotationY(viewId, degreeHorizontal)
                it.setRotationX(viewId, degreeVertical)
                it.setRotation(viewId, degreeAround)
                it.setFloatValue(viewId, Attribute.RADIUS, radius)
                it.setFloatValue(viewId, Attribute.ALPHA, opacity)
                it.setFloatValue(viewId, Attribute.CARD_ELEVATION, containerElevation)
            }
    }

    private fun applyShadowCustomization() {
        this@SlideMenuLayout.getConstraintSet(R.id.weatherElapsed)
            .let {
                it.setFloatValue(shadowMock.id, Attribute.ALPHA, 0f)
                it.setIntValue(shadowMock.id, Attribute.CARD_COLOR, shadow.color)
            }
        this@SlideMenuLayout.getConstraintSet(R.id.weatherCollapsed)
            .let {
                it.setFloatValue(shadowMock.id, Attribute.ALPHA, shadow.opacity)
                it.setIntValue(shadowMock.id, Attribute.CARD_COLOR, shadow.color)
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
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
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
            RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
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
            setOnClickListener(this@SlideMenuLayout)
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
        this@SlideMenuLayout.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                enableViews(p0!!.progress == 1f)
            }
        })
    }



}