package com.txs.artandroid.c1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import com.txs.artandroid.R
import com.txs.artandroid.c2.MessengerActivity

class C1point3Activity : AppCompatActivity() {
    private lateinit var tv: TextView
    private lateinit var mTvMessenger: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c1point3)
        tv = findViewById<TextView>(R.id.tv_jump)
        mTvMessenger = findViewById<TextView>(R.id.tv_messenger)
        tv.setOnClickListener {
            val intent = Intent()
            intent.action = "com.art.charpter.a"
            intent.addCategory("com.art.charpter_1.c")
            intent.setDataAndType(Uri.parse("content://abc"), "text/plain")
            val pm = this.packageManager
            val cn = intent.resolveActivity(pm)
            //判断是否匹配
            if (cn == null) {
                Toast.makeText(this, "no activity", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(intent)
            }
        }

        mTvMessenger.setOnClickListener {
            startActivity(Intent(this@C1point3Activity,MessengerActivity::class.java))
        }

    }
}
