package com.a.kotlin_library.demo2

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


class HomeAdapter internal constructor(
        private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_home_item_view, parent, false)
        return PartViewHolder(view)
    }

    fun updateDataAsync(data: List<HomeTable>) {
        asyncListDiffer.submitList(data)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PartViewHolder).bind(context, asyncListDiffer.currentList[position])
    }


    private val itemCallback = object : DiffUtil.ItemCallback<HomeTable>() {
        override fun areItemsTheSame(oldItem: HomeTable, newItem: HomeTable): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HomeTable, newItem: HomeTable): Boolean {
            return oldItem.desc == newItem.desc
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, itemCallback)

    class PartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(context: Context, homeTable: HomeTable) {

//            Glide.with(context)
//                    .load(homeTable.envelopePic)
//                    .error(R.drawable.ic_baseline_error_24)
//                    .into(itemView.image1)
//
//            itemView.title1.text = homeTable.title
//            itemView.overview1.text = homeTable.desc

            val intent = Intent(context, HomeChildActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("homeTable", homeTable)
            intent.putExtras(bundle)
            itemView.setOnClickListener {
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = asyncListDiffer.currentList.size
}

