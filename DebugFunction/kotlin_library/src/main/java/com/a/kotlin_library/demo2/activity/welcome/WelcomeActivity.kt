package com.a.kotlin_library.demo2.activity.welcome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.a.kotlin_library.R
import com.a.kotlin_library.demo2.activity.HomeActivity
import com.a.kotlin_library.demo2.activity.base.BaseActivity
import com.a.kotlin_library.demo2.utils.MmkvUtil
import com.a.kotlin_library.demo2.utils.gone
import com.a.kotlin_library.demo2.utils.visible
import com.a.kotlin_library.demo2.view_model.WelcomeViewModel
import com.a.kotlin_library.demo2.widget.banner.WelcomeBannerViewHolder
import com.zhpan.bannerview.BannerViewPager
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.activity_welcome.view.*


class WelcomeActivity : BaseActivity<com.a.kotlin_library.databinding.ActivityWelcomeBinding, WelcomeViewModel>() {
    private val TAG = "WelcomeActivity"
    private var pageTip = arrayOf("One Page", "Two Page", "Three Page")
    private lateinit var mViewPager: BannerViewPager<String, WelcomeBannerViewHolder>
    override fun layoutId() = R.layout.activity_welcome

    override fun beforeInit(savedInstanceState: Bundle?) {
        actionBar?.hide()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun bindViewModel() {
        binding.listener = Listener()
    }

    override fun initView(savedInstanceState: Bundle?) {
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT !== 0) {
            finish()
            return
        }
        var first: Boolean = MmkvUtil.isFirst()
        first = true
        if (first) {
            mViewPager = findViewById(R.id.banner_view)
            mViewPager.apply {
                adapter = WelcomeBannerAdapter()
                setLifecycleRegistry(lifecycle)
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        updateEnterVisible(position)
                    }

                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                        updateEnterVisible(position)
                    }
                }).create(pageTip.toList())
            }
        } else {
            welcome_image.visible()
            mViewPager.postDelayed({
                toMainPage()
            }, 300)
        }
    }

    inner class Listener {
        fun navToMain() {
            Log.d(TAG, "navToMain(Listener.kt:77)--->>")
            MmkvUtil.setFirst(false)
            toMainPage()
        }
    }

    private fun updateEnterVisible(position: Int) {
        if (position == pageTip.size - 1) {
            welcomeEnter?.visible()
        } else {
            welcomeEnter?.gone()
        }
    }

    private fun toMainPage() {
        Log.d(TAG, "toMainPage(WelcomeActivity.kt:73)--->>")
        startActivity(Intent(this@WelcomeActivity, HomeActivity::class.java))
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun observe() {

    }


}
