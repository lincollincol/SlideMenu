package linc.com.slidemenu.util

import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import linc.com.slidemenu.R

internal object MotionConnector {
    private var constraintSet: ConstraintSet? = null
    private var layout: MotionLayout? = null

    fun setParentLayout(layout: MotionLayout) {
        this.layout = layout
    }

    fun setConstraintSet(constraintSetId: Int) {
        this.constraintSet = layout?.getConstraintSet(constraintSetId)
    }

    fun topToTopOf(actionViewId: Int, fixedViewId: Int) = connect(actionViewId, fixedViewId, TOP, TOP)
    fun bottomToTopOf(actionViewId: Int, fixedViewId: Int) = connect(actionViewId, fixedViewId, BOTTOM, TOP)
    fun topToBottomOf(actionViewId: Int, fixedViewId: Int) = connect(actionViewId, fixedViewId, TOP, BOTTOM)
    fun bottomToBottomOf(actionViewId: Int, fixedViewId: Int) = connect(actionViewId, fixedViewId, BOTTOM, BOTTOM)
    fun startToStartOf(actionViewId: Int, fixedViewId: Int) = connect(actionViewId, fixedViewId, START, START)
    fun endToStartOf(actionViewId: Int, fixedViewId: Int) = connect(actionViewId, fixedViewId, END, START)
    fun startToEndOf(actionViewId: Int, fixedViewId: Int) = connect(actionViewId, fixedViewId, START, END)
    fun endToEndOf(actionViewId: Int, fixedViewId: Int) = connect(actionViewId, fixedViewId, END, END)

    fun allToView(actionViewId: Int, baseViewId: Int) {
        connect(actionViewId, baseViewId, TOP, TOP)
        connect(actionViewId, baseViewId, BOTTOM, BOTTOM)
        connect(actionViewId, baseViewId, START, START)
        connect(actionViewId, baseViewId, END, END)
    }

    fun removeConnection(viewId: Int, side: Int) {
        constraintSet?.clear(viewId, START)
    }

    fun clearConnections(viewId: Int) {
        constraintSet?.let {
            it.clear(viewId, START)
            it.clear(viewId, END)
            it.clear(viewId, TOP)
            it.clear(viewId, BOTTOM)
        }
    }

    private fun connect(
        actionViewId: Int,
        fixedViewId: Int,
        actionViewSide: Int,
        fixedViewSide: Int
    ) {
        constraintSet?.let {
            it.connect(actionViewId, actionViewSide, fixedViewId, fixedViewSide)
            it.applyTo(layout)
        }
    }

}