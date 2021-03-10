package com.a.kotlin_library.room2.db

//仓库，每个操作者进入仓库都有操作者独自的权限
class SubscriberRepository(private val dao: SubscriberDAO) {

    val subscribers = dao.getAllSubscribers()

    suspend fun insert(subscriber: Subscriber): Long {
        return dao.insertSubscriber(subscriber)
    }

    suspend fun update(subscriber: Subscriber): Int {
        return dao.updateSubscriber(subscriber)
    }

    suspend fun delete(subscriber: Subscriber): Int {
        return dao.deleteSubscriber(subscriber)
    }

    suspend fun deleteAll(): Int {
        return dao.deleteAll()
    }
}
