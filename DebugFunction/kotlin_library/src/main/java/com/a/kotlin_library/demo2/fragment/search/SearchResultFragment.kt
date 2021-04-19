//package com.a.kotlin_library.demo2.fragment.search
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.a.kotlin_library.R
//import com.a.kotlin_library.databinding.FragmentSearchResultBinding
//import com.a.kotlin_library.demo2.fragment.base.BaseFragment
//import com.a.kotlin_library.demo2.fragment.init
//import com.a.kotlin_library.demo2.fragment.initClose
//import com.a.kotlin_library.demo2.fragment.initFloatBtn
//import com.a.kotlin_library.demo2.fragment.showLoading
//import com.a.kotlin_library.demo2.utils.SettingUtil
//import com.a.kotlin_library.demo2.utils.appContext
//import com.a.kotlin_library.demo2.utils.nav
//import com.a.kotlin_library.demo2.utils.navigateAction
//import com.blankj.utilcode.util.ConvertUtils
//import com.kingja.loadsir.core.LoadService
//import com.kingja.loadsir.core.LoadSir
//import com.yanzhenjie.recyclerview.SwipeRecyclerView
//import kotlinx.android.synthetic.main.include_recyclerview.*
//import kotlinx.android.synthetic.main.include_toolbar.*
//
///**
// * A simple [Fragment] subclass.
// * Use the [SearchResultFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class SearchResultFragment : BaseFragment<FragmentSearchResultBinding, SearchResultViewModel>() {
//    private var searchKey = ""
//
//    //界面状态管理者
//    private lateinit var loadService: LoadService<Any>
//
//    //收藏viewModel
//    private val searchViewModel: SearchViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            searchKey = it.getString("searchKey").toString()
//        }
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_search_result, container, false)
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment SearchResultFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//                SearchResultFragment().apply {
//                    arguments = Bundle().apply {
//                        putString(ARG_PARAM1, param1)
//                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//
//    override fun layoutId(): Int {
//        return R.layout.fragment_search_result
//    }
//
//    override fun initView(savedInstanceState: Bundle?) {
//        toolbar.initClose(searchKey) {
//            nav().navigateUp()
//        }
//        //状态页配置
//        loadService = loadServiceInit(swipeRefresh) {
//            //点击重试时触发的操作
//            loadService.showLoading()
//            searchViewModel.getSearchResultData(searchKey, true)
//        }
//
//        //初始化recyclerView
//        recyclerView.init(LinearLayoutManager(mContext), articleAdapter).let {
//            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
//            it.initFooter(SwipeRecyclerView.LoadMoreListener {
//                //触发加载更多时请求数据
//                viewModel.getSearchResultData(searchKey, false)
//            })
//            //初始化FloatingActionButton
//            it.initFloatBtn(floatbtn)
//        }
//        //初始化 SwipeRefreshLayout
//        swipeRefresh.init {
//            //触发刷新监听时请求数据
//            viewModel.getSearchResultData(searchKey, true)
//        }
//
//        articleAdapter.run {
//            setCollectClick { item, v, position ->
//                if (v.isChecked) {
//                    requestCollectViewModel.uncollect(item.id)
//                } else {
//                    requestCollectViewModel.collect(item.id)
//                }
//            }
//            setOnItemClickListener { adapter, view, position ->
//                nav().navigateAction(R.id.action_to_webFragment, Bundle().apply {
//                    putParcelable("ariticleData", articleAdapter.data[position])
//                })
//            }
//            addChildClickViewIds(R.id.item_home_author, R.id.item_project_author)
//            setOnItemChildClickListener { adapter, view, position ->
//                when (view.id) {
//                    R.id.item_home_author, R.id.item_project_author -> {
//                        nav().navigateAction(
//                                R.id.action_searchResultFragment_to_lookInfoFragment,
//                                Bundle().apply {
//                                    putInt("id", articleAdapter.data[position].userId)
//                                })
//                    }
//                }
//            }
//        }
//    }
//}
//
//fun loadServiceInit(view: View, callback: () -> Unit): LoadService<Any> {
//    val loadsir = LoadSir.getDefault().register(view) {
//        //点击重试时触发的操作
//        callback.invoke()
//    }
//    loadsir.showSuccess()
//    SettingUtil.setLoadingColor(SettingUtil.getColor(appContext), loadsir)
//    return loadsir
//}
