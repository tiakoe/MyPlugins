package com.a.kotlin_library.demo2.activity.other2

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.a.kotlin_library.R
import com.a.kotlin_library.databinding.ActivityHomeChildPageBinding
import com.a.kotlin_library.demo2.view_model.ChildViewModel
import com.a.kotlin_library.room.table.HomeTable
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home_child_page.*

class TestHomeChildActivity : AppCompatActivity() {
    var uploadMessage: ValueCallback<Array<Uri>>? = null
    val REQUEST_SELECT_FILE = 100

    private lateinit var binding: ActivityHomeChildPageBinding
    private lateinit var viewModel: ChildViewModel
    private lateinit var webSettings: WebSettings
    private lateinit var webClient: WebViewClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_child_page)
        viewModel = ViewModelProvider(this).get(ChildViewModel::class.java)
        binding.dataChild = viewModel
        configWebView()
        initWebViewClient()
        initWebView()

        back.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            finishActivity(12)
        }
    }

    private fun initWebView() {
        val homeTable = intent.getParcelableExtra<HomeTable>("homeTable")
        var webUrl = homeTable?.link
        if (webUrl == null) {
            webUrl = intent.getStringExtra("url")
        }
        binding.webView.webViewClient = webClient
        binding.webView.fitsSystemWindows = true
        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webUrl.let { it?.let { it1 -> binding.webView.loadUrl(it1) } }
    }

    private fun initWebViewClient() {
        webClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                request?.run {
                    url.path?.let { toSysWeb(it) }
                }
                return true
            }
        }
    }

    private fun toSysWeb(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(this, TestHomeChildActivity::class.java)
        intent.putExtra("url", uri)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        println("----xxx---resultCode: " + resultCode)
        if (requestCode == REQUEST_SELECT_FILE) {
            uploadMessage?.let {
                uploadMessage!!.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent))
                uploadMessage = null
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configWebView() {
        webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT

        webSettings.setSupportZoom(false) // 支持缩放 默认为true 是下面那个的前提
        webSettings.builtInZoomControls = false
        webSettings.displayZoomControls = false

        webSettings.blockNetworkImage = true
        webSettings.loadsImagesAutomatically = true

        webSettings.safeBrowsingEnabled = true

        webSettings.javaScriptCanOpenWindowsAutomatically = true // 支持通过JS打开新窗口
        webSettings.domStorageEnabled = true // 启用或禁用DOM缓存
        webSettings.setSupportMultipleWindows(true)

        webSettings.useWideViewPort = true  // 将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true  // 缩放至屏幕的大小
        webSettings.allowFileAccess = true

        webSettings.setGeolocationEnabled(true)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return if (binding.webView.canGoBack()) {
                binding.webView.goBack()
                true
            } else {
                finish()
                true
            }
        }
        return false
    }
}

