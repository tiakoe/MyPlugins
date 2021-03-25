package com.a.kotlin_library.demo2.activity.welcome

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.a.kotlin_library.R
import com.a.kotlin_library.databinding.ActivityWelcomeBinding
import com.a.kotlin_library.demo2.activity.MainActivity
import com.a.kotlin_library.demo2.activity.base.BaseActivity
import com.a.kotlin_library.demo2.utils.MmkvUtil
import com.a.kotlin_library.demo2.view_model.WelcomeViewModel
import com.a.kotlin_library.demo2.widget.banner.WelcomeBannerViewHolder
import com.zhpan.bannerview.BannerViewPager
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.activity_welcome.view.*
import java.lang.reflect.ParameterizedType


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class WelcomeActivity : BaseActivity<ActivityWelcomeBinding, WelcomeViewModel>() {
    private val TAG = "WelcomeActivity"
    private var pageTip = arrayOf("One Page", "Two Page", "Three Page")
    private lateinit var mViewPager: BannerViewPager<String, WelcomeBannerViewHolder>
    override fun layoutId() = R.layout.activity_welcome

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(getVmClazz(this))
        initView()
    }

    @Suppress("UNCHECKED_CAST")
    fun <VM> getVmClazz(obj: Any): VM {
        Log.d(TAG, "getVmClazz(WelcomeActivity.kt:38)--->>" + (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0].typeName)
        Log.d(TAG, "getVmClazz(WelcomeActivity.kt:40)--->>" + (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1].typeName)
        return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as VM
    }


    inner class Listener {
        fun navToMain() {
            MmkvUtil.setFirst(false)
            toMainPage()
        }

    }

    fun initView() {
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT !== 0) {
            finish()
            return
        }
//        welcome_base_view.setBackgroundColor(Color.parseColor("#f34649"))
        if (MmkvUtil.isFirst()) {
            mViewPager = findViewById(R.id.banner_view)
            mViewPager.apply {
                adapter = WelcomeBannerAdapter()
                setLifecycleRegistry(lifecycle)
                create(pageTip.toList())
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        if (position == pageTip.size - 1) {
                            welcomeEnter?.visibility = View.VISIBLE
                        } else {
                            welcomeEnter?.visibility = View.GONE
                        }
                    }
                })
            }
        } else {
            welcome_image.visibility = View.VISIBLE
            mViewPager.postDelayed({
                toMainPage()
            }, 300)
        }
    }

    private fun toMainPage() {
        startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

}
