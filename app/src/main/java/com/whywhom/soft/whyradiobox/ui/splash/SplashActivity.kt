package com.whywhom.soft.whyradiobox.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.constraintlayout.motion.widget.MotionLayout
import com.whywhom.soft.whyradiobox.R
import com.whywhom.soft.whyradiobox.extensions.extEdit
import com.whywhom.soft.whyradiobox.extensions.sharedPreferences
import com.whywhom.soft.whyradiobox.ui.BaseActivity
import com.whywhom.soft.whyradiobox.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashActivity : BaseActivity() {
    private var mMotionLayout: MotionLayout? = null

    private val splashDuration = 3 * 1000L

    private val alphaAnimation by lazy {
        AlphaAnimation(0.5f, 1.0f).apply {
            duration = splashDuration
            fillAfter = true
        }
    }

    private val scaleAnimation by lazy {
        ScaleAnimation(1f, 1.05f, 1f, 1.05f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
            duration = splashDuration
            fillAfter = true
        }
    }

    /**
     * Whether or not to enter the APP for the first time
     */
    var isFirstEntryApp: Boolean
        get() = sharedPreferences.getBoolean("is_first_entry_app", true)
        set(value) = sharedPreferences.extEdit { putBoolean("is_first_entry_app", value) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        iv_background.startAnimation(alphaAnimation)
        iv_background.startAnimation(scaleAnimation)
        apply {
            GlobalScope.launch { // launch a new coroutine in background and continue
                delay(3000L)
                startActivity(Intent(this@SplashActivity,MainActivity::class.java))
                this@SplashActivity.finish()
            }
        }
    }

//    override fun onWindowFocusChanged(hasFocus: Boolean) {
//        super.onWindowFocusChanged(hasFocus)
//        if (hasFocus) {
//            hideSystemUIAndNavigation(this)
//        }
//    }

//    private fun hideSystemUIAndNavigation(activity: Activity) {
//        val decorView: View = activity.window.decorView
//        decorView.systemUiVisibility =
//            (
//                    View.SYSTEM_UI_FLAG_IMMERSIVE
//                            // Set the content to appear under the system bars so that the
//                    // content doesn't resize when the system bars hide and show.
//                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Hide the nav bar and status bar
//                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
//    }

}
