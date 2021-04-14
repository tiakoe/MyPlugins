package com.a.kotlin_library.test2

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

class SingleThreadExecutorDispatcher
    : AbstractCoroutineContextElement(ContinuationInterceptor),
        ContinuationInterceptor {

    private val executorService = Executors.newSingleThreadExecutor {
        Thread(it, "SingleThreadExecutor")
    }

    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
        return MyContinuation(continuation)
    }

    inner class MyContinuation<T>(private val origin: Continuation<T>) : Continuation<T> {
        override val context: CoroutineContext = origin.context
        override fun resumeWith(result: Result<T>) {
            executorService.submit {
                origin.resumeWith(result)
            }
        }
    }

    fun close() {
        executorService.shutdown()
    }
}

fun main() = runBlocking {
    val dispatcher = SingleThreadExecutorDispatcher()
    log("AA")
    val job = GlobalScope.launch(dispatcher) {
        delay(100)
        log("BB")
    }
    job.join()
    dispatcher.close()
}
