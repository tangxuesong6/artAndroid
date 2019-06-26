package com.txs.artandroid.c2.binderpool

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.txs.artandroid.R
import java.lang.Exception

class BinderPoolActivity : AppCompatActivity() {
    private lateinit var mSecurityCenter: ISecurityCenter
    private lateinit var mCompute: ICompute

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_binder_pool)
        Thread(Runnable { doWork() }).start()
    }

    private fun doWork() {
        val binderPool = BinderPool.instance(this)
        val securityBinder = binderPool.queryBinder(BINDER_SECURITY_CENTER)
        mSecurityCenter = ISecurityCenter.Stub
            .asInterface(securityBinder) as ISecurityCenter
        val msg = "hello android"
        println("content: $msg")
        try {
            val password = mSecurityCenter.encrypt(msg)
            println("encrypt: $password")
            println("decrypt: ${mSecurityCenter.decrypt(password)}")
        }catch (e:Exception){

        }
        val computeBinder = binderPool.queryBinder(BINDER_COMPUTE)
        mCompute = ComputeImpl.asInterface(computeBinder)
        try {
            println("3 + 5 = ${mCompute.add(3,5)}")
        }catch (e:Exception){}
    }
}
