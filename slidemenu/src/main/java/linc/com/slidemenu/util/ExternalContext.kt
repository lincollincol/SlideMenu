package linc.com.slidemenu.util

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.lang.ClassCastException

class ExternalContext(private val context: Context) {
    private val parent: Parent

    init {
        this.parent = try {
            (context as Fragment)
            Parent.FRAGMENT
        } catch (cce: ClassCastException) {
            Parent.ACTIVITY
        }
    }

    fun getActivity() = context as FragmentActivity
    fun getFragment() = context as Fragment

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