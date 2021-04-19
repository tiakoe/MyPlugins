package com.a.kotlin_library.demo2.fragment.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.a.kotlin_library.R
import com.a.kotlin_library.demo2.activity.other2.HomeChildActivity
import com.a.kotlin_library.room.table.HomeTable
import kotlinx.android.synthetic.main.activity_home_item_view.view.*


class HomeFragmentAdapter internal constructor(
        private val context: Context
) : RecyclerView.Adapter<HomeFragmentAdapter.ItemViewHolder>() {

    var list = ArrayList<HomeTable>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_home_item_view, parent, false)
        return ItemViewHolder(view)
    }


    fun updateDataAsync(data: List<HomeTable>, isClear: Boolean = false) {
//        asyncListDiffer.submitList(data)
        if (isClear) {
            list.clear()
        }
        val pos = list.size
        list.addAll(data)
        if (!isClear) {
            notifyItemInserted(pos - 1)
        } else {
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(context, list[position], position)
//        holder.bind(context, asyncListDiffer.currentList[position], position)
    }


    private val itemCallback = object : DiffUtil.ItemCallback<HomeTable>() {
        override fun areItemsTheSame(oldItem: HomeTable, newItem: HomeTable): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: HomeTable, newItem: HomeTable): Boolean {
            return true
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, itemCallback)

    inner class ItemViewHolder constructor(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(context: Context, homeTable: HomeTable, position: Int) {

            itemView.index.text = position.toString()
            itemView.title.text = homeTable.title

            if (homeTable.author != "") {
                itemView.author.text = homeTable.author
            } else {
                itemView.author.text = "wan"
            }
            itemView.niceDate.text = homeTable.niceDate
            itemView.superChapterName.text = homeTable.superChapterName
            itemView.chapterName.text = ">" + homeTable.chapterName

            val intent = Intent(context, HomeChildActivity::class.java)
            val bundle = Bundle()
            bundle.putString("url", homeTable.link)
            bundle.putString("title", homeTable.title)
            intent.putExtras(bundle)
            itemView.setOnClickListener {
                context.startActivity(intent)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return list.size
//        return asyncListDiffer.currentList.size
    }


}


