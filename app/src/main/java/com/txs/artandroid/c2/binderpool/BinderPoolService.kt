package com.txs.artandroid.c2.binderpool

import android.app.Service
import android.content.Intent
import android.os.IBinder

class BinderPoolService : Service() {
    val TAG = BinderPoolService::class.java.simpleName
    val mBinderPool = BinderPool.BinderPoolImpl()

    override fun onBind(intent: Intent): IBinder {
        return mBinderPool
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
