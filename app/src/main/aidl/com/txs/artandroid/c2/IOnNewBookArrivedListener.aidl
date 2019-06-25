// IOnNewBookArrivedListener.aidl
package com.txs.artandroid.c2;
import com.txs.artandroid.c2.Book;

// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {
   void onNewBookArrived(in Book newBook);
}
