// IBinderPool.aidl
package com.txs.artandroid.c2.binderpool;

// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
