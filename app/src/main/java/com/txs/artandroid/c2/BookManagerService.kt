package com.txs.artandroid.c2

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.RemoteCallbackList
import android.os.RemoteException
import android.util.Log
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

class BookManagerService : Service() {
    private val TAG = "BMS"
    val copyOnWriteArrayList: CopyOnWriteArrayList<Book> = CopyOnWriteArrayList<Book>()
    private var mIsServiceDestroryed: AtomicBoolean = AtomicBoolean(false)
    val mListenerList = RemoteCallbackList<IOnNewBookArrivedListener>()

    private val mBinder = object : IBookManager.Stub() {
        override fun registerListener(listener: IOnNewBookArrivedListener?) {
            mListenerList.register(listener)
        }

        override fun unregisterListener(listener: IOnNewBookArrivedListener?) {
            mListenerList.unregister(listener)
        }

        override fun getBookList(): List<Book>? {
            return copyOnWriteArrayList
        }

        @Throws(RemoteException::class)
        override fun addBook(book: Book) {
            copyOnWriteArrayList.add(book)

        }
    }


    override fun onBind(intent: Intent): IBinder? {
        val check = checkCallingOrSelfPermission("com.txs.artandroid.permission.ACCESS_BOOK_SERVICE")
        if (check == PackageManager.PERMISSION_DENIED){
            return null
        }

        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        copyOnWriteArrayList.add(Book(1, "android"))
        copyOnWriteArrayList.add(Book(2, "ios"))
        Thread(ServiceWorker()).start()
    }

    override fun onDestroy() {
        mIsServiceDestroryed.set(true)
        super.onDestroy()
    }

    @Throws(RemoteException::class)
    private fun onNewBookArrived(book: Book) {
        copyOnWriteArrayList.add(book)
        val N = mListenerList.beginBroadcast()
        for (i in 0 until N) {
            val l = mListenerList.getBroadcastItem(i)
            if (l != null) {
                try {
                    l.onNewBookArrived(book)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
        }
        mListenerList.finishBroadcast()
    }

    private inner class ServiceWorker : Runnable {
        override fun run() {
            while (!mIsServiceDestroryed.get()) {
                try {
                    Thread.sleep(5000)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val bookId = copyOnWriteArrayList.size + 1
                val newBook = Book(bookId, "new book# $bookId")
                try {
                    onNewBookArrived(newBook)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
