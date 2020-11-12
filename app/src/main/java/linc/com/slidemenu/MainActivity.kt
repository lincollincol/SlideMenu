package linc.com.slidemenu

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import linc.com.slidemenu.models.CollapseSide
import linc.com.slidemenu.models.MenuItem
import linc.com.slidemenu.models.Section
import linc.com.slidemenu.models.Shadow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.slide_menu_layout)

        val sm = SlideMenu.Builder()
            .withContext(this)
            .setContentFragment(ContentFragment())
            .setMenuSide(CollapseSide.END)
            // HEADER
            .addMenuItem(MenuItem(R.layout.menu_view, MenuItem.START, Section.HEADER, "aa"))
            .addMenuItem(MenuItem(R.layout.menu_view, MenuItem.CENTER, Section.HEADER, "aa"))
            .addMenuItem(MenuItem(R.layout.menu_view, MenuItem.END, Section.HEADER, "aa"))
            // FOOTER
            .addMenuItem(MenuItem(R.layout.menu_view, MenuItem.START, Section.FOOTER, "aa"))
            .addMenuItem(MenuItem(R.layout.menu_view, MenuItem.CENTER, Section.FOOTER, "aa"))
            .addMenuItem(MenuItem(R.layout.menu_view, MenuItem.END, Section.FOOTER, "aa"))
            // Controllers
            .addMenuItem(MenuItem(R.layout.menu_view, MenuItem.START, Section.CONTROLLER, "aa"))
            .addMenuItem(MenuItem(R.layout.menu_view, MenuItem.START, Section.CONTROLLER, "aa"))
            .addMenuItem(MenuItem(R.layout.menu_view, MenuItem.START, Section.CONTROLLER, "aa"))
            .addMenuItem(MenuItem(R.layout.menu_view, MenuItem.START, Section.CONTROLLER, "aa"))
            .addMenuItem(MenuItem(R.layout.menu_view, MenuItem.START, Section.CONTROLLER, "aa"))
            .addMenuItem(MenuItem(R.layout.menu_view, MenuItem.START, Section.CONTROLLER, "aa"))
            .setContentShadow(Shadow(Color.GRAY, 0.5f))
            .setControllerSectionGravity(Gravity.BOTTOM)
            .highlightDragView(true)
            .build()


    }
}