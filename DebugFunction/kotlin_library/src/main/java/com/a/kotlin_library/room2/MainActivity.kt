package com.a.kotlin_library.room2

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.a.kotlin_library.R
import com.a.kotlin_library.room2.db.Subscriber
import com.a.kotlin_library.room2.db.SubscriberDatabase
import com.a.kotlin_library.room2.db.SubscriberRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: com.a.kotlin_library.databinding.ActivityMain2Binding
    private lateinit var subscriberViewModel: SubscriberViewModel
    private lateinit var adapter: MyRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_2)

        val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)
        subscriberViewModel = ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)

        binding.myViewModel = subscriberViewModel
        binding.lifecycleOwner = this
        initRecyclerView()

        subscriberViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun initRecyclerView() {
        binding.subscriberRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyRecyclerViewAdapter({ selectedItem: Subscriber -> listItemClicked(selectedItem) })
        binding.subscriberRecyclerView.adapter = adapter
        displaySubscribersList()
    }

    private fun displaySubscribersList() {
        subscriberViewModel.subscribers.observe(this, Observer {
            Log.i("MYTAG", it.toString())
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(subscriber: Subscriber) {
        //Toast.makeText(this,"selected name is ${subscriber.name}",Toast.LENGTH_LONG).show()
        subscriberViewModel.initUpdateAndDelete(subscriber)
    }
}
