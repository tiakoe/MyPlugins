package com.a.kotlin_library.demo2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.a.kotlin_library.R
import com.a.kotlin_library.demo2.activity.HomeChildActivity
import com.a.kotlin_library.demo2.bean.DataX
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_home_item_view.view.*

class HomeAdapter internal constructor(
        private val context: Context,
        private val dataList: List<DataX>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_home_item_view, parent, false)
        return PartViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PartViewHolder).bind(context, dataList[position])
    }

    class PartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(context: Context, dataX: DataX) {
            Glide.with(context)
                    .load(dataX.envelopePic)
                    .error(R.drawable.ic_baseline_error_24)
                    .into(itemView.image1)

            itemView.title1.text = dataX.title
            itemView.overview1.text = dataX.desc

            val intent = Intent(context, HomeChildActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("dataX", dataX)
            intent.putExtras(bundle)
            itemView.setOnClickListener {
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = dataList.size
}

