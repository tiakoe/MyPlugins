package com.a.kotlin_library.demo2.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
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

    }

    override fun bindViewModel() {

    }


    override fun initView(savedInstanceState: Bundle?) {
        Log.d(TAG, "initView(HomeActivity.kt:22)--->>")
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
