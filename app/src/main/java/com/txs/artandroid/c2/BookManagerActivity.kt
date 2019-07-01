package com.txs.artandroid.c2

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.LinearLayout
import com.txs.artandroid.R

class BookManagerActivity : AppCompatActivity() {
    val TAG: String = BookManagerActivity::class.java.simpleName
    val MESSAGE_NEW_BOOK_ARRIVED = 1
    private lateinit var mRemoteBookManager: IBookManager
    private val mConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {

        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val bookManager = IBookManager.Stub.asInterface(p1)
            mRemoteBookManager = bookManager
            val list = bookManager.bookList
            Log.d(TAG, "query book list, list type:" + list.javaClass.canonicalName)
            val newBook = Book(3, "android 开发艺术探索")
            bookManager.addBook(newBook)
            Log.d(TAG, "add book:$newBook")
            val newList = bookManager.bookList
            newList.forEach { Log.d(TAG, "query book list:${it.bookName}") }
            bookManager.registerListener(mOnNewBookArrivedListener)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_manager)
        val intent = Intent(this, BookManagerService::class.java)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        if (mRemoteBookManager.asBinder().isBinderAlive) {
            try {
                Log.d(TAG, "unregister listener: $mOnNewBookArrivedListener")
                mRemoteBookManager.unregisterListener(mOnNewBookArrivedListener)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }

        }

        unbindService(mConnection)
        super.onDestroy()
    }

    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                MESSAGE_NEW_BOOK_ARRIVED -> {
                    Log.d(TAG, "receive new book : ${msg.obj}   ${(msg.obj as Book).bookId}")
                }
                else -> {
                    super.handleMessage(msg)
                }
            }

        }
    }
    private val mOnNewBookArrivedListener: IOnNewBookArrivedListener = object : IOnNewBookArrivedListener.Stub() {
        override fun onNewBookArrived(newBook: Book?) {
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, newBook).sendToTarget()
        }
    }

}
