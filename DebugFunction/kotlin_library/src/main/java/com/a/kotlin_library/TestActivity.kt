//package com.a.kotlin_library
//
//import android.app.Activity
//import android.content.Context
//import android.os.Bundle
//import android.view.View
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//
//class TestActivity : AppCompatActivity() {
//    //kotlin 封装：
//    private fun <V : View> Activity.bindView(id: Int): Lazy<V> = lazy {
//        viewFinder(id) as V
//    }
//
//    private val viewFinder: Activity.(Int) -> View? get() = { findViewById(it) }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        init(this)
//
//        //在activity中的使用姿势
//        val mTextView by bindView<TextView>(R.id.text_view)
//        mTextView.text = "执行到我时，才会进行控件初始化"
//
//        var (province, city) = getData(2)
//        Toast.makeText(this, province + "-" + city, Toast.LENGTH_LONG).show()
//    }
//
//    fun getData(id: Int): Pair<String, String> {
//        if (id < 5) {
//            return "湖北" to "武汉"
//        } else {
//            return "广东" to "深圳"
//        }
//    }
//
//    fun init(context: Context) {
//        val statusBarHeight = ResourcesUtils.getStatusBarHeight(context)
//        Toast.makeText(context, "状态栏高度为：$statusBarHeight", Toast.LENGTH_SHORT).show()
//    }
//
//}
//
