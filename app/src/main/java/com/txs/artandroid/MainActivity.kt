package com.txs.artandroid

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.txs.artandroid.c1.C1point3Activity
import com.txs.artandroid.c2.BookManagerActivity
import com.txs.artandroid.c2.provider.ProviderActivity
import com.txs.artandroid.c2.socket.TCPClientActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mTvC1Filter: TextView
    private lateinit var mTvAidl: TextView
    private lateinit var mTvProvider: TextView
    private lateinit var mTvSocket: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTvC1Filter = findViewById<TextView>(R.id.tv_c1_filter)
        mTvAidl = findViewById<TextView>(R.id.tv_aidl)
        mTvProvider = findViewById<TextView>(R.id.tv_provider)
        mTvSocket = findViewById<TextView>(R.id.tv_socket)

        mTvC1Filter.setOnClickListener(this)
        mTvAidl.setOnClickListener(this)
        mTvProvider.setOnClickListener(this)
        mTvSocket.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_c1_filter -> {
                startActivity(Intent(this@MainActivity, C1point3Activity::class.java))
            }
            R.id.tv_aidl -> {
                startActivity(Intent(this@MainActivity, BookManagerActivity::class.java))
            }
            R.id.tv_provider -> {
                startActivity(Intent(this@MainActivity, ProviderActivity::class.java))
            }
            R.id.tv_socket -> {
                startActivity(Intent(this@MainActivity,TCPClientActivity::class.java))
            }
        }
    }

}