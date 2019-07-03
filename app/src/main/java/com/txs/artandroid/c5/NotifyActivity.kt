package com.txs.artandroid.c5

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.RemoteViews
import android.widget.TextView
import com.txs.artandroid.R
import com.txs.artandroid.c4.DemoActivity_2
import java.net.URL

class NotifyActivity : AppCompatActivity() {
    private lateinit var mTvNotifyNormal: TextView
    private lateinit var mTvNotifyRemote: TextView
    private lateinit var bitmap:Bitmap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify)
        mTvNotifyNormal = findViewById<TextView>(R.id.tv_notify_normal)
        mTvNotifyRemote = findViewById<TextView>(R.id.tv_notify_remote)
        Thread(Runnable {
            val url = URL("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1562075462221&di=9f98a784856e59db747730c9e7b0c5d4&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201610%2F30%2F20161030190441_ef3YE.jpeg")
            bitmap = BitmapFactory.decodeStream(url.openStream())
        }).start()
        mTvNotifyNormal.setOnClickListener {
            normalNotify()
        }
        mTvNotifyRemote.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                remoteNotify()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun remoteNotify() {
        val remoteView = RemoteViews(packageName,R.layout.layout_notification)
        remoteView.setTextViewText(R.id.tv_one,"one")
        remoteView.setTextViewText(R.id.tv_two,"two")
        remoteView.setImageViewBitmap(R.id.imgv_one,bitmap)
        remoteView.setImageViewResource(R.id.imgv_two,R.mipmap.icon)
        var notification = Notification()
        val intent = Intent(this, DemoActivity_2::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("1234", "channel", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        } else {
        }
        val icon = Icon.createWithBitmap(bitmap)
        val builder = Notification.Builder(this, notification)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setAutoCancel(true)
                .setChannelId("1234")
                .setContentIntent(pendingIntent)
                .setSmallIcon(icon)
                .setWhen(System.currentTimeMillis())
//                .setCustomContentView(remoteView)
                .setCustomBigContentView(remoteView)
                .setOngoing(true)
        }
        manager.notify(1, notification)
    }

    private fun normalNotify() {
        var notification = Notification()
        val intent = Intent(this, DemoActivity_2::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("1234", "channel", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        } else {
        }
        val builder = Notification.Builder(this, notification)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setAutoCancel(true)
                .setContentTitle("hello")
                .setContentText("hahaha")
                .setChannelId("1234")
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setOngoing(false)
        }

        manager.notify(1, notification)
    }
}
