package com.txs.artandroid.c2.provider

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.txs.artandroid.R

class ProviderActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider)
        val uri = Uri.parse("content://com.txs.android.c2.provider/book")
        val bookCursor =  contentResolver.query(uri, arrayOf("_id","name"), null,null,null)
        while (bookCursor.moveToNext()){
            Log.d(ProviderActivity::class.java.simpleName,"query book: ${bookCursor.getString(1)}")
        }
        contentResolver.query(uri,null, null,null,null)
        contentResolver.query(uri,null, null,null,null)
    }
}
