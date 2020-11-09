package linc.com.slidemenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import linc.com.slidemenu.util.MenuSide

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.slide_menu_layout)

        val sm = SlideMenu.Builder()
            .withContext(this)
            .setContentFragment(ContentFragment())
            .setMenuSide(MenuSide.START)
            .build()


    }
}