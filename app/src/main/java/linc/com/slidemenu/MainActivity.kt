package linc.com.slidemenu

import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import linc.com.slidemenu.models.CollapseSide
import linc.com.slidemenu.models.MenuItem
import linc.com.slidemenu.models.Section
import linc.com.slidemenu.models.Shadow

class MainActivity : AppCompatActivity() {

//    private lateinit var slideMenu: SlideMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.slide_menu_layout)
        setContentView(R.layout.activity_main)

        /*slideMenu = SlideMenu.Builder()
            .withContext(this)
            .setContentFragment(ContentFragment())
            .setMenuSide(CollapseSide.START)
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
            .build()*/

//        slideMenu.handleConfiguration(Configuration().apply {
//            orientation = Configuration.ORIENTATION_LANDSCAPE
//        });
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
//        slideMenu.handleConfiguration(newConfig)
    }
}