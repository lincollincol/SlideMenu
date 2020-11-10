package linc.com.slidemenu

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import linc.com.slidemenu.util.MenuSide
import linc.com.slidemenu.util.Shadow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.slide_menu_layout)

        val sm = SlideMenu.Builder()
            .withContext(this)
            .setContentFragment(ContentFragment())
            .setMenuSide(MenuSide.START)
            .setContentShadow(Shadow(Color.GRAY, 0.5f))
            .highlightDragView(true)
            .build()



    }
}