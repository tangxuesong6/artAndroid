package com.txs.artandroid.c1

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.TextView

class FilterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread(Runnable {
            val root = LinearLayout(this@FilterActivity)
            root.orientation = LinearLayout.VERTICAL
            val textView = TextView(this@FilterActivity)
            root.addView(textView)
            textView.text = "子线程UI"
            setContentView(root)
            Thread.sleep(3000)
            textView.text = "子线程更新UI"
        }).start()
    }
}
