package linc.com.slidemenu

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentContainer

class SlideContainerView(
    context: Context,
    attrs: AttributeSet
) : CardView(context, attrs) {

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.SlideContainerView)

        println("ELEVATION = ${attributes.getInteger(R.styleable.SlideContainerView_collapseElevation, -1)}")
        println("OPACITY = ${attributes.getFloat(R.styleable.SlideContainerView_collapseOpacity, -1f)}")
        println("RADIUS = ${attributes.getFloat(R.styleable.SlideContainerView_collapseCornersRadius, -1f)}")
        println("HORIZONTAL = ${attributes.getFloat(R.styleable.SlideContainerView_rotationHorizontalDegree, -1f)}")
        println("VERTICAL = ${attributes.getFloat(R.styleable.SlideContainerView_rotationVerticalDegree, -1f)}")
        println("AROUND = ${attributes.getFloat(R.styleable.SlideContainerView_rotationAroundDegree, -1f)}")

        attributes.recycle()
    }


}