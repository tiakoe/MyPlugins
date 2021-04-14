package com.a.kotlin_library.demo2.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.a.kotlin_library.R
import com.a.kotlin_library.demo2.activity.other2.TestHomeChildActivity
import com.a.kotlin_library.room.table.HomeTable
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_home_item_view.view.*
import java.util.concurrent.LinkedBlockingDeque
import kotlin.concurrent.thread


class TestAdapter internal constructor(
        private val context: Context,
        private val dataList: List<HomeTable>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataSet: ArrayList<HomeTable> = dataList as ArrayList

    private val pendingUpdates: LinkedBlockingDeque<List<HomeTable>> = LinkedBlockingDeque<List<HomeTable>>()

    private var mHugeNum = 10


    private val mHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_home_item_view, parent, false)
        return PartViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        dataSet
        (holder as PartViewHolder).bind(context, dataSet[position])
    }

//    fun addDataSet(position: Int) {
//        //添加动画
//        notifyItemInserted(position);
//    }
//
//    fun removeDataSet(position: Int) {
//        //删除动画
//        notifyItemRemoved(position);
//        notifyDataSetChanged();
//    }

    fun updateDataAsync(data: List<HomeTable>) {
        asyncListDiffer.submitList(data)
    }

    //    异步差异比较
    private val itemCallback = object : DiffUtil.ItemCallback<HomeTable>() {
        override fun areItemsTheSame(oldItem: HomeTable, newItem: HomeTable): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HomeTable, newItem: HomeTable): Boolean {
            return oldItem.desc == newItem.desc
        }
    }
    private val asyncListDiffer = AsyncListDiffer(this, itemCallback)

    private fun updateInternal(newData: List<HomeTable>) {
        if (newData.size > mHugeNum) {
            thread(name = "diffCallback") {
                val diffCallback = DiffUtilCallBack(this.dataSet, newData)
                val diffResult = DiffUtil.calculateDiff(diffCallback, true)
                mHandler.post {
                    dispatchUpdates(newData, diffResult)
                }
            }.start()
        } else {
            val diffCallback = DiffUtilCallBack(this.dataSet, newData)
            val diffResult = DiffUtil.calculateDiff(diffCallback, true)
            dispatchUpdates(newData, diffResult)
        }
    }

    private fun dispatchUpdates(newList: List<HomeTable>, diffResult: DiffUtil.DiffResult) {
        if (pendingUpdates.size > 0) {
            pendingUpdates.remove()
        }
//
        this.dataSet.clear()
        this.dataSet.addAll(newList)
        diffResult.dispatchUpdatesTo(this)

        if (pendingUpdates.isNotEmpty()) {
            lateinit var lastList: List<HomeTable>
            if (pendingUpdates.size > 1) {
                lastList = pendingUpdates.removeLast()
                pendingUpdates.clear()
            }
            updateInternal(lastList)
        }
    }

    //    主线程更新
    fun updateDataAtMain(newData: List<HomeTable>) {
        val diffResult = DiffUtil.calculateDiff(DiffUtilCallBack(dataSet, newData))
        dataSet.clear()
        dataSet.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    //    子线程更新或者主线程更新
    fun updateData(newData: List<HomeTable>) {
        pendingUpdates.add(newData)
        if (pendingUpdates.size > 1) {
            return
        }
        updateInternal(newData)
    }

    class PartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(context: Context, homeTable: HomeTable) {

            Glide.with(context)
                    .load(homeTable.envelopePic)
                    .error(R.drawable.ic_baseline_error_24)
                    .into(itemView.image1)

            itemView.title1.text = homeTable.title
            itemView.overview1.text = homeTable.desc

            val intent = Intent(context, TestHomeChildActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("homeTable", homeTable)
            intent.putExtras(bundle)
            itemView.setOnClickListener {
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = dataSet.size
}

