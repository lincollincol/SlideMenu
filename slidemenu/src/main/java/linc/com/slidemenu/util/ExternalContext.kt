package linc.com.slidemenu.util

import android.content.Context
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import linc.com.slidemenu.R
import java.lang.ClassCastException

internal object ExternalContext {

    private lateinit var parent: Parent
    private lateinit var activity: FragmentActivity
    private lateinit var fragment: Fragment


    fun init(context: Context) {
        this.parent = try {
            (context as Fragment)
            fragment = context as Fragment
            Parent.FRAGMENT
        } catch (cce: ClassCastException) {
            activity = context as FragmentActivity
            Parent.ACTIVITY
        }
    }

    fun getActivity() = activity

    fun getFragment() = fragment

    fun <T : View> findViewById(id: Int) : T {
        return when {
            isActivity() -> getActivity().findViewById(id)
            else -> getFragment().view!!.findViewById(id)
        }
    }

    fun isActivity() = parent == Parent.ACTIVITY

    enum class Parent {
        FRAGMENT, ACTIVITY
    }
}
/*
package linc.com.slidemenu.util

import android.content.Context

object ExternalContext {

    private lateinit var parent: Parent

    fun init(parent: Parent) {
        this.parent = parent
    }

    fun isActivity() = parent == Parent.ACTIVITY

    enum class Parent {
        FRAGMENT, ACTIVITY
    }
}
 */