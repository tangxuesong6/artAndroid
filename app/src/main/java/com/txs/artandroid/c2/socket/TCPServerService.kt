package com.txs.artandroid.c2.socket

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class TCPServerService : Service() {
    private var mIsServiceDestroyed = false
    private val mDefinedMessages = arrayOf(
        "hello,programer",
        "what is your name ?",
        " The weather is ok",
        "do you know, i can talk somebody together",
        "take a jock: it's pleasure who like smile."
    )

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        Thread(TCPServer()).start()
        super.onCreate()
    }

    override fun onDestroy() {
        mIsServiceDestroyed = true
        super.onDestroy()
    }

    inner class TCPServer : Runnable {
        override fun run() {
            var serverSocket: ServerSocket? = null
            try {
                serverSocket = ServerSocket(8688)
            } catch (e: Exception) {
                println("establish tcp server failed, port:8688")
                return
            }
            while (!mIsServiceDestroyed) {
                try {
                    val client = serverSocket.accept()
                    println("accept")
                    responseClient(client)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun responseClient(client: Socket?) {
        val inputStream = BufferedReader(InputStreamReader(client?.getInputStream()))
        val outputStream = PrintWriter(BufferedWriter(OutputStreamWriter(client?.getOutputStream())), true)
        outputStream.println("welcome to chatroom")
        while (!mIsServiceDestroyed) {
            val str = inputStream.readLine()
            println("msg from client: $str")
            if (str == null) {
                break
            }
            val i = Random().nextInt(mDefinedMessages.size)
            val msg = mDefinedMessages[i]
            outputStream.println(msg)
            println("send : $msg")
        }
        println("client quit .")
        outputStream.close()
        inputStream.close()
        client?.close()
    }
}



















