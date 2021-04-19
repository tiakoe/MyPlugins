package com.a.kotlin_library.demo2.fragment.search

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.a.kotlin_library.R
import com.a.kotlin_library.databinding.FragmentSearchBinding
import com.a.kotlin_library.demo2.exception.AppException
import com.a.kotlin_library.demo2.fragment.base.BaseFragment
import com.a.kotlin_library.demo2.fragment.init
import com.a.kotlin_library.demo2.fragment.initClose
import com.a.kotlin_library.demo2.utils.MmkvUtil.setSearchHistoryData
import com.a.kotlin_library.demo2.utils.SettingUtil
import com.a.kotlin_library.demo2.utils.nav
import com.a.kotlin_library.demo2.utils.navigateAction
import com.a.kotlin_library.demo2.utils.toJson
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.include_toolbar.*


/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>() {

    private val hotAdapter: SearcHotAdapter by lazy { SearcHotAdapter(arrayListOf()) }
    private val historyAdapter: SearcHistoryAdapter by lazy { SearcHistoryAdapter(arrayListOf()) }

    private var loadingDialog: MaterialDialog? = null

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchView = menu.findItem(R.id.searchView)?.actionView as SearchView
        searchView.run {
            maxWidth = 30
            onActionViewExpanded()
            queryHint = "输入关键字搜索"
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    //当点击搜索时 输入法的搜索，和右边的搜索都会触发
                    query?.let { queryStr ->
                        updateKey(queryStr)
                        nav().navigateAction(R.id.action_searchFragment_to_searchResultFragment,
                                Bundle().apply {
                                    putString("searchKey", queryStr)
                                }
                        )
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
            isSubmitButtonEnabled = true //右边是否展示搜索图标
            val field = javaClass.getDeclaredField("mGoButton")
            field.run {
                isAccessible = true
                val mGoButton = get(searchView) as ImageView
                mGoButton.setImageResource(R.drawable.ic_baseline_search_24)
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * 更新搜索词
     */
    fun updateKey(keyStr: String) {
        viewModel.historyData.value?.let {
            if (it.contains(keyStr)) {
                //当搜索历史中包含该数据时 删除
                it.remove(keyStr)
            } else if (it.size >= 10) {
                //如果集合的size 有10个以上了，删除最后一个
                it.removeAt(it.size - 1)
            }
            //添加新数据到第一条
            it.add(0, keyStr)
            viewModel.historyData.value = it
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SearchFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }

    private fun createObserver() {
        viewModel.run {
            //监听热门数据变化
            hotData.observe(viewLifecycleOwner, { resultState ->
                parseState(resultState, {
                    hotAdapter.setList(it)
                })
            })
            //监听历史数据变化
            historyData.observe(viewLifecycleOwner, {
                historyAdapter.data = it
                historyAdapter.notifyDataSetChanged()
                setSearchHistoryData(it.toJson())
            })
        }
    }

    fun <T> BaseFragment<*, *>.parseState(
            resultState: ResultState<T>,
            onSuccess: (T) -> Unit,
            onError: ((AppException) -> Unit)? = null,
            onLoading: (() -> Unit)? = null
    ) {
        when (resultState) {
            is ResultState.Loading -> {
                showLoading(resultState.loadingMessage)
                onLoading?.invoke()
            }
            is ResultState.Success -> {
                dismissLoading()
                onSuccess(resultState.data)
            }
            is ResultState.Error -> {
                dismissLoading()
                onError?.run { this(resultState.error) }
            }
        }
    }

    fun Fragment.showLoading(message: String = "请求网络中") {
        activity?.let {
            if (!it.isFinishing) {
                if (loadingDialog == null) {
                    loadingDialog = MaterialDialog(it)
                            .cancelable(true)
                            .cancelOnTouchOutside(false)
                            .cornerRadius(12f)
                            .customView(R.layout.layout_custom_progress_dialog_view)
                            .lifecycleOwner(this)
                    loadingDialog?.getCustomView()?.run {
                        this.findViewById<TextView>(R.id.loading_tips).text = message
                        this.findViewById<ProgressBar>(R.id.progressBar).indeterminateTintList = SettingUtil.getOneColorStateList(it)
                    }
                }
                loadingDialog?.show()
            }
        }
    }

    fun Fragment.showLoadingExt(message: String = "请求网络中") {
        activity?.let {
            if (!it.isFinishing) {
                if (loadingDialog == null) {
                    loadingDialog = MaterialDialog(it)
                            .cancelable(true)
                            .cancelOnTouchOutside(false)
                            .cornerRadius(12f)
                            .customView(R.layout.layout_custom_progress_dialog_view)
                            .lifecycleOwner(this)
                    loadingDialog?.getCustomView()?.run {
                        this.findViewById<TextView>(R.id.loading_tips).text = message
                        this.findViewById<ProgressBar>(R.id.progressBar).indeterminateTintList = SettingUtil.getOneColorStateList(it)
                    }
                }
                loadingDialog?.show()
            }
        }
    }


    fun Activity.dismissLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    fun Fragment.dismissLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    override fun layoutId(): Int {
        return R.layout.fragment_search
    }

    override fun initView(savedInstanceState: Bundle?) {
        initToolBar()
        createObserver()
        setMenu()
//        appViewModel.appColor.value?.let { setUiTheme(it, search_text1, search_text2) }
        //初始化搜搜历史Recyclerview
        search_historyRv.init(LinearLayoutManager(mContext), historyAdapter, false)
        //初始化热门Recyclerview
        val layoutManager = FlexboxLayoutManager(mContext)
        //方向 主轴为水平方向，起点在左端
        layoutManager.flexDirection = FlexDirection.ROW
        //左对齐
        layoutManager.justifyContent = JustifyContent.FLEX_START
        search_hotRv.init(layoutManager, hotAdapter, false)

        historyAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                val queryStr = historyAdapter.data[position]
                updateKey(queryStr)
                nav().navigateAction(R.id.action_searchFragment_to_searchResultFragment,
                        Bundle().apply {
                            putString("searchKey", queryStr)
                        }
                )
            }
            addChildClickViewIds(R.id.item_history_del)
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.item_history_del -> {
                        viewModel.historyData.value?.let {
                            it.removeAt(position)
                            viewModel.historyData.value = it
                        }
                    }
                }
            }
        }

        hotAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                val queryStr = hotAdapter.data[position].name
                updateKey(queryStr)
                nav().navigateAction(R.id.action_searchFragment_to_searchResultFragment,
                        Bundle().apply {
                            putString("searchKey", queryStr)
                        }
                )
            }
        }

        search_clear.setOnClickListener {
            activity?.let {
                MaterialDialog(it)
                        .cancelable(false)
                        .lifecycleOwner(this)
                        .show {
                            title(text = "温馨提示")
                            message(text = "确定清空吗?")
                            negativeButton(text = "取消")
                            positiveButton(text = "清空") {
                                //清空
                                viewModel.historyData.value = arrayListOf()
                            }
                            getActionButton(WhichButton.POSITIVE).updateTextColor(
                                    SettingUtil.getColor(
                                            it
                                    )
                            )
                            getActionButton(WhichButton.NEGATIVE).updateTextColor(
                                    SettingUtil.getColor(
                                            it
                                    )
                            )
                        }
            }
        }
    }

    private fun initToolBar() {
        toolbar.run {
            initClose {
                inflateMenu(R.menu.search_menu)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.searchView -> {
                            nav().navigateAction(R.id.action_searchFragment_to_searchResultFragment)
                        }
                    }
                    true
                }
            }
        }
    }

    private fun setMenu() {
        setHasOptionsMenu(true)
        toolbar.run {
            mContext.setSupportActionBar(this)
            initClose {
                nav().navigateUp()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mContext.setSupportActionBar(null)
    }
}
