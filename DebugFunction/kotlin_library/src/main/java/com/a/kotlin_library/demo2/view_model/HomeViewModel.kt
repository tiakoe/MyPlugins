package com.a.kotlin_library.demo2.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.a.kotlin_library.demo2.room.HomeDatabase
import com.a.kotlin_library.demo2.room.HomeRepository
import com.a.kotlin_library.demo2.room.HomeTable
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HomeRepository
    val allHomeData: LiveData<List<HomeTable>>
    private val database: HomeDatabase = HomeDatabase.getDatabase(application)

    init {
        val homeDao = database.homeDao()
        repository = HomeRepository(homeDao)
        allHomeData = repository.allHomeData
    }

    fun insert(homeTable: HomeTable) = viewModelScope.launch {
        repository.insert(homeTable)
    }


    fun insertAll(datas: List<HomeTable>) = viewModelScope.launch {
        repository.insertAll(datas)
    }

    fun update(homeTable: HomeTable) = viewModelScope.launch {
        repository.update(homeTable)
    }

    fun delete() = viewModelScope.launch {
        repository.delete()
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

}
