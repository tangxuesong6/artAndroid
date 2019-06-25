package com.txs.artandroid.c2.provider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Author: txs
 * Date: 19-6-24 下午7:18
 * Package:com.txs.artandroid.c2.provider
 */
const val DB_NAME = "book_provider.db"
const val DB_VERSION = 1
const val BOOK_TABLE_NAME = "book"
const val USER_TABLE_NAME = "user"

class DbOpenHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    private val CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS $BOOK_TABLE_NAME (_id INTEGER PRIMARY KEY, name TEXT)"
    private val CREATE_USER_TABLE =
        "CREATE TABLE IF NOT EXISTS $USER_TABLE_NAME (_id INTEGER PRIMARY KEY, name TEXT, sex INT)"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_BOOK_TABLE)
        p0?.execSQL(CREATE_USER_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}