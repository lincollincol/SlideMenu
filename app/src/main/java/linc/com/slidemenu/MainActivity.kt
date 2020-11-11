package linc.com.slidemenu

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            .setMenuSide(CollapseSide.START)
            .addMenuItem(MenuItem(R.layout.menu_view, MenuItem.END, Section.HEADER, "aa"))
            .addMenuItem(MenuItem(R.layout.menu_view, MenuItem.START, Section.FOOTER, "aa"))
            .setContentShadow(Shadow(Color.GRAY, 0.5f))
            .highlightDragView(true)
            .build()



    }
}