package com.txs.artandroid.c5

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.SystemClock
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.txs.artandroid.R

/**
 * Author: txs
 * Date: 19-7-3 上午11:33
 * Package:com.txs.artandroid.c5
 */
class MyAppWidgetProvider : AppWidgetProvider() {
    val TAG = MyAppWidgetProvider::class.java.simpleName
    val CLICK_ACTION = "com.txs.action.CLICK"
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.d(TAG, "onReceive: action = ${intent?.action}")
        if (intent?.action.equals(CLICK_ACTION)) {
            Toast.makeText(context, "clicked it", Toast.LENGTH_SHORT).show()
            Thread(Runnable {
                val srcbBitmap = BitmapFactory.decodeResource(context?.resources, R.mipmap.icon)
                val appWidgetManager = AppWidgetManager.getInstance(context)
                for (i in 0 until 37) {
                    val degree : Float = ((i * 10) % 360).toFloat()
                    val remoteViews = RemoteViews(context?.packageName,R.layout.widget)
                    remoteViews.setImageViewBitmap(R.id.imageView1,rotateBitmap(context!!,srcbBitmap,degree))
                    val intentClick = Intent()
                    intentClick.setAction(CLICK_ACTION)
                    intentClick.component = ComponentName(context, MyAppWidgetProvider::class.java)

                    val pendingIntent = PendingIntent.getBroadcast(context,0,intentClick,0)
                    remoteViews.setOnClickPendingIntent(R.id.imageView1,pendingIntent)
                    appWidgetManager.updateAppWidget(ComponentName(context,MyAppWidgetProvider::class.java),remoteViews)
                    SystemClock.sleep(30)
                }
            }).start()
        }
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.d(TAG,"update")
        val counter = appWidgetIds?.size
        Log.d(TAG,"counter = $counter")
        for (i in 0 until counter!!){
            val appWidgetId = appWidgetIds[i]
            onWidgetUpdate(context,appWidgetManager,appWidgetId)
        }
    }

    private fun onWidgetUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetId: Int) {
        Log.d(TAG,"appwidgetId = $appWidgetId")
        val remoteViews = RemoteViews(context?.packageName,R.layout.widget)
        val intentClick = Intent()
        intentClick.setAction(CLICK_ACTION)
        intentClick.component = ComponentName(context, MyAppWidgetProvider::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,0,intentClick,0)
        remoteViews.setOnClickPendingIntent(R.id.imageView1,pendingIntent)
        appWidgetManager?.updateAppWidget(appWidgetId,remoteViews)
    }


    private fun rotateBitmap(context: Context,srcBitmap:Bitmap,degree:Float) : Bitmap{
        val matrix = Matrix()
        matrix.reset()
        matrix.setRotate(degree)
        val tmpBitmap = Bitmap.createBitmap(srcBitmap,0,0,srcBitmap.width,srcBitmap.height,matrix,true)
        return tmpBitmap
    }
}