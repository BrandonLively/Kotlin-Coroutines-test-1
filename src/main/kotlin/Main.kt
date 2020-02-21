import kotlinx.coroutines.*
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

fun main(args: Array<String>){
    exampleLaunchCoroutineScope()
}

suspend fun printlnDelayed(message: String){
    delay(1000)
    println(message)
}

fun exampleBlocking() = runBlocking{
    println("one")
    printlnDelayed("two")
    println("three")
}

fun exampleBlockingDispatcher(){
    runBlocking(Dispatchers.Default) {
        println("One - from thread ${Thread.currentThread().name}")
        printlnDelayed("Two - from thread ${Thread.currentThread().name}")
        }
    println("Three - from thread ${Thread.currentThread().name}")
}

fun exampleLaunchGlobal() = runBlocking {
    println("one - from thread ${Thread.currentThread().name}")
    GlobalScope.launch {
        printlnDelayed("Two - from thread ${Thread.currentThread().name}")
    }
    println("Three - from thread ${Thread.currentThread().name}")
    delay(3000)
}

fun exampleLaunchGlobalWaiting() = runBlocking {
    println("one - from thread ${Thread.currentThread().name}")
    val job = GlobalScope.launch {
        printlnDelayed("Two - from thread ${Thread.currentThread().name}")
    }
    println("Three - from thread ${Thread.currentThread().name}")
    job.join()
}

fun exampleLaunchCoroutineScope() = runBlocking {
    println("one - from thread ${Thread.currentThread().name}")

    val customDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()

    launch(customDispatcher) {
        printlnDelayed("Two - from thread ${Thread.currentThread().name}")
    }
    println("Three - from thread ${Thread.currentThread().name}")

    (customDispatcher.executor as ExecutorService).shutdown()
}