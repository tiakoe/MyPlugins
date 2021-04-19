package com.a.kotlin_library.demo2.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowInsets
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.navigation.Navigation
import com.a.kotlin_library.R
import com.a.kotlin_library.databinding.ActivityHomeBinding
import com.a.kotlin_library.demo2.activity.base.BaseActivity
import com.a.kotlin_library.demo2.view_model.HomeActivityViewModel
import com.blankj.utilcode.util.ToastUtils


class HomeActivity : BaseActivity<ActivityHomeBinding, HomeActivityViewModel>() {
    private val TAG = "HomeActivity"
    override fun layoutId() = R.layout.activity_home

    override fun beforeInit(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        mActivity.window.statusBarColor = getColor(R.color.translucent);
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    override fun bindViewModel() {

    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun initSystemBarsByAndroidX() {
        val controller = ViewCompat.getWindowInsetsController(window.decorView)
//        // 设置状态栏反色
//        controller?.isAppearanceLightStatusBars = true
//        // 取消状态栏反色
//        controller?.isAppearanceLightStatusBars = false
//        // 设置导航栏反色
//        controller?.isAppearanceLightNavigationBars = true
//        // 取消导航栏反色
//        controller?.isAppearanceLightNavigationBars = false
        // 隐藏状态栏
//        controller?.hide(WindowInsets.Type.statusBars())
        // 显示状态栏
        controller?.show(WindowInsets.Type.statusBars())
        // 隐藏导航栏
        controller?.hide(WindowInsets.Type.navigationBars())
        // 显示导航栏
//        controller?.show(WindowInsets.Type.navigationBars())
        // 同时隐藏状态栏和导航栏
//        controller?.hide(WindowInsets.Type.systemBars())
//        // 同时隐藏状态栏和导航栏
//        controller?.show(WindowInsets.Type.systemBars())
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun initView(savedInstanceState: Bundle?) {
        Log.d(TAG, "initView(HomeActivity.kt:22)--->>")
        initSystemBarsByAndroidX()
//        mActivity.window?.run {
//            WindowCompat.setDecorFitsSystemWindows(this, false)
//        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.insetsController?.hide(WindowInsets.Type.navigationBars())
//        } else {
//            @Suppress("DEPRECATION")
//            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        }

        //进入首页检查更新
//        Beta.checkUpgrade(false, true)
        var exitTime = 0L
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d(TAG, "handleOnBackPressed(HomeActivity.kt:23)--->>")
                val nav = Navigation.findNavController(this@HomeActivity, R.id.host_fragment)
                if (nav.currentDestination != null && nav.currentDestination!!.id != R.id.main_fragment) {
                    //如果当前界面不是主页，那么直接调用返回即可
                    nav.navigateUp()
                } else {
                    //是主页
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        ToastUtils.showShort("再按一次退出程序")
                        exitTime = System.currentTimeMillis()
                    } else {
                        finish()
                    }
                }
            }
        })
//        全局
//        appViewModel.appColor.value?.let {
//            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
//            supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
//            StatusBarUtil.setColor(this, it, 0)
//        }
    }

    override fun observe() {
    }

//    override fun createObserver() {
//        appViewModel.appColor.observeInActivity(this, Observer {
//            supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
//            StatusBarUtil.setColor(this, it, 0)
//        })
//    }

//    override fun onNetworkStateChanged(netState: NetState) {
//        super.onNetworkStateChanged(netState)
//        if (netState.isSuccess) {
//            Toast.makeText(applicationContext, "我特么终于有网了啊!", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(applicationContext, "我特么怎么断网了!", Toast.LENGTH_SHORT).show()
//        }
//    }

}
