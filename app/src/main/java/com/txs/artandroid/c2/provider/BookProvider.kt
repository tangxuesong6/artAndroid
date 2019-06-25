package com.txs.artandroid.c2.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Log

class BookProvider : ContentProvider() {
    private val TAG = BookProvider::class.java.simpleName
    val AUTHORITY = "com.txs.android.c2.provider"
    val BOOK_CONTENT_URI = Uri.parse("content://$AUTHORITY/book")
    val USER_CONTENT_URI = Uri.parse("content://$AUTHORITY/user")
    val BOOK_URI_CODE = 0
    val USER_URI_CODE = 1
    var uriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    var mDb: SQLiteDatabase? = null

    init {
        uriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE)
        uriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE)
    }


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        Log.d(TAG, "delete")
        return 0
    }

    override fun getType(uri: Uri): String? {
        Log.d(TAG, "getType")
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.d(TAG, "insert")
        return null
    }

    override fun onCreate(): Boolean {
        Log.d(TAG, "onCreate, current thread: ${Thread.currentThread().name}")
        mDb = DbOpenHelper(context).writableDatabase
        mDb.let { it?.execSQL("delete from book") }
        mDb.let { it?.execSQL("insert into book values(3,'Android');") }
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        Log.d(TAG, "query, current thread: ${Thread.currentThread().name}")
        val table = getTableName(uri)
        print("hehe")
        return mDb?.query(table, projection, selection, selectionArgs, null, null, sortOrder, null)
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        Log.d(TAG, "update")
        return 0
    }

    private fun getTableName(uri: Uri): String? {
        var tableName: String? = ""
        when (uriMatcher.match(uri)) {
            BOOK_URI_CODE -> {
                tableName = BOOK_TABLE_NAME
            }
            USER_URI_CODE -> {
                tableName = USER_TABLE_NAME
            }
        }
        return tableName
    }
}
