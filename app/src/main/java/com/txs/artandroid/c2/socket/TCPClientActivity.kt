package com.txs.artandroid.c2.socket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.EditText
import android.widget.TextView
import com.txs.artandroid.R
import com.txs.artandroid.c2.binderpool.BinderPool
import java.io.*
import java.lang.ref.WeakReference
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*

class TCPClientActivity : AppCompatActivity() {
    private lateinit var mTvContent: TextView
    private lateinit var mEdtMsg: EditText
    private lateinit var mTvSend: TextView
    val MESSAGE_RECEIVE_NEW_MSG = 1
    val MESSAGE_SOCKET_CONNECTED = 2
    private var mPrintWriter: PrintWriter? = null
    private var mClientSocket: Socket? = null
    private val mHandler = MyHandler(WeakReference(this))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tcpclient)
        mTvContent = findViewById<TextView>(R.id.tv_content)
        mEdtMsg = findViewById<EditText>(R.id.edt_msg)
        mTvSend = findViewById<TextView>(R.id.tv_send)
        val service = Intent(this, TCPServerService::class.java)
        startService(service)
        Thread(Runnable {
            connectTCPServer()
        }).start()
        mTvSend.setOnClickListener {
            val msg = mEdtMsg.text.toString()
            if (!TextUtils.isEmpty(msg) && mPrintWriter != null) {
                Thread(Runnable {
                    mPrintWriter?.println(msg)
                }).start()
                mEdtMsg.setText("")
                val time = formatDateTime(System.currentTimeMillis())
                val showedMsg = "self $time : $msg \n"
                mTvContent.setText("${mTvContent.text} $showedMsg")
            }

        }
    }

    inner class MyHandler(private val activity: WeakReference<TCPClientActivity>) : Handler() {
        override fun handleMessage(msg: Message?) = when (msg?.what) {
            MESSAGE_RECEIVE_NEW_MSG -> {
                mTvContent.text = "${mTvContent.text}" + "${(msg.obj as String)}"
            }
            MESSAGE_SOCKET_CONNECTED -> {
                mTvSend.isEnabled = true
            }
            else -> {
                super.handleMessage(msg)
            }
        }
    }

    override fun onDestroy() {
        if (mClientSocket != null) {
            try {
                mClientSocket?.shutdownInput()
                mClientSocket?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        super.onDestroy()
    }

    @SuppressLint("SimpleDateFormat")
    private fun formatDateTime(time: Long): String {
        return SimpleDateFormat("(HH:mm:ss)").format(Date(time))
    }

    private fun connectTCPServer() {
        var socket: Socket? = null
        while (socket == null) {
            try {
                socket = Socket("localhost", 8688)
                mClientSocket = socket
                mPrintWriter = PrintWriter(BufferedWriter(OutputStreamWriter(socket.getOutputStream())), true)
                mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED)
                println("connect server success")
            } catch (e: IOException) {
                SystemClock.sleep(1000)
                println("connect tcp server failed, retry...")
            }
        }
        try {
            val br = BufferedReader(InputStreamReader(socket.getInputStream()))
            while (!this@TCPClientActivity.isFinishing) {
                val msg = br.readLine()
                println("receive: $msg")
                if (msg != null) {
                    val time = formatDateTime(System.currentTimeMillis())
                    val showedMsg = "server $time : $msg \n"
                    mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, showedMsg).sendToTarget()
                }
            }
            println("quit ...")
            mPrintWriter?.close()
            br.close()
            socket.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

}
