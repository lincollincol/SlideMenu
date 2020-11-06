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

/*
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.motion.widget.MotionLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:layoutDescription="@xml/scene_slide_menu_start"
    app:applyMotionScene="true"
    app:motionDebug="SHOW_ALL">

    <View
        android:id="@+id/dragView"
        android:layout_width="200dp"
        android:layout_height="300dp"
        android:background="#ccffdd"
        android:elevation="15dp"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"/>

    <FrameLayout
        android:id="@+id/contentFragment"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:background="#00ffcc"
        app:layout_constraintTop_toTopOf="@id/contentHorizontalTopLine"
        app:layout_constraintBottom_toBottomOf="@id/contentHorizontalBottomLine"
        app:layout_constraintStart_toStartOf="@id/contentVerticalStartLine"
        app:layout_constraintEnd_toEndOf="@id/contentVerticalEndLine"/>

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/contentHorizontalTopLine"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:layout_constraintGuide_percent="0"/> <!-- 0 -->

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/contentHorizontalBottomLine"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:layout_constraintGuide_percent="1"/><!-- 1 -->

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/contentVerticalStartLine"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:layout_constraintGuide_percent="0"/> <!-- 0 -->

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/contentVerticalEndLine"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:layout_constraintGuide_percent="1"/> <!-- 1 -->



</androidx.constraintlayout.motion.widget.MotionLayout>
 */