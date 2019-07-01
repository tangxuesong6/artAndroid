package com.txs.artandroid.c3

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import com.txs.artandroid.R

class ScrollActivity : AppCompatActivity() {
    private lateinit var mTv: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll)
        mTv = findViewById<TextView>(R.id.tv)
        mTv.setOnClickListener {
            val params = mTv.layoutParams as ViewGroup.MarginLayoutParams
//            params.width += 100
            params.leftMargin += 100
            mTv.requestLayout()
        }

    }
}
