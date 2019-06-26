package com.txs.artandroid.c2.binderpool

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import java.util.concurrent.CountDownLatch

/**
 * Author: txs
 * Date: 19-6-25 下午7:24
 * Package:com.txs.artandroid.c2.binderpool
 */
const val BINDER_NONE = -1
const val BINDER_COMPUTE = 0
const val BINDER_SECURITY_CENTER = 1

class BinderPool {
    private val TAG = BinderPool::class.java.simpleName

    private var mContext: Context? = null
    private var mBinderPool: IBinderPool? = null
    private lateinit var mConnectBinderPoolCountDownLatch: CountDownLatch
    constructor(context: Context){
        mContext = context.applicationContext
        connectBinderPoolService()
    }
    @Synchronized
    private fun connectBinderPoolService() {
        mConnectBinderPoolCountDownLatch = CountDownLatch(1)
        val service = Intent(mContext, BinderPoolService::class.java)
        mContext?.bindService(service, mBinderPoolConnection, Context.BIND_AUTO_CREATE)
        try {
            mConnectBinderPoolCountDownLatch.await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    public fun queryBinder(binderCode: Int): IBinder? {
        var binder: IBinder? = null
        try {
            if (mBinderPool != null) {
                binder = mBinderPool?.queryBinder(binderCode)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return binder
    }

    companion object {
        fun instance(context: Context): BinderPool {
            val obj: BinderPool by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
                BinderPool(context)
            }
            return obj
        }
    }

    private val mBinderPoolConnection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {

        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            mBinderPool = IBinderPool.Stub.asInterface(p1)
            try {
                mBinderPool?.asBinder()?.linkToDeath(mBinderPoolDeathRecipient, 0)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            mConnectBinderPoolCountDownLatch.countDown()
        }
    }

    private val mBinderPoolDeathRecipient = object : IBinder.DeathRecipient {
        override fun binderDied() {
            Log.d(TAG, "binder died.")
            mBinderPool?.asBinder()?.unlinkToDeath(this, 0)
            mBinderPool = null
            connectBinderPoolService()
        }
    }

    class BinderPoolImpl : IBinderPool.Stub() {

        override fun queryBinder(binderCode: Int): IBinder? {
            var binder: IBinder? = null
            when (binderCode) {
                BINDER_SECURITY_CENTER -> {
                    binder = SecurityCenterImplKotlin()
                }
                BINDER_COMPUTE -> {
                    binder = ComputeImplKotlin()
                }
            }
            return binder
        }
    }
}