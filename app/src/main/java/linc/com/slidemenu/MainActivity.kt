 package linc.com.slidemenu

import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import kotlinx.android.synthetic.main.activity_main.*
import linc.com.slidemenu.models.CollapseSide
import linc.com.slidemenu.models.MenuItem
import linc.com.slidemenu.models.Section
import linc.com.slidemenu.models.Shadow

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        slide.shadow = Shadow(Color.BLACK, 0.4f)
        slide.highlightDrag = true
        slide.refresh()


        btn.setOnClickListener {
            if(slide.side == CollapseSide.START)
                slide.side = CollapseSide.END
            else
                slide.side = CollapseSide.START
            slide.radius = if(slide.radius != 100f) 100f else 0f
            slide.refresh()
        }

        /*supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.fragment_view, ContentFragment())
            .commit()*/

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

}