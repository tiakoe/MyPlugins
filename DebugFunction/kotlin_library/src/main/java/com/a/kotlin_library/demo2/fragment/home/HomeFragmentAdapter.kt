package com.a.kotlin_library.demo2.fragment.home

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
import com.a.kotlin_library.demo2.activity.other2.TestHomeChildActivity
import com.a.kotlin_library.room.table.HomeTable
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_home_item_view.view.*


class HomeFragmentAdapter internal constructor(
        private val context: Context
) : RecyclerView.Adapter<HomeFragmentAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_home_item_view, parent, false)
        return ItemViewHolder(view)
    }


    fun updateDataAsync(data: List<HomeTable>) {
        asyncListDiffer.submitList(data)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(context, asyncListDiffer.currentList[position], position)
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
        fun bind(context: Context, homeTable: HomeTable, position: Int) {
            Glide.with(context)
                    .load(homeTable.envelopePic)
                    .error(R.drawable.ic_baseline_error_24)
                    .into(itemView.image1)

            itemView.index.text = position.toString()
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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }


}


