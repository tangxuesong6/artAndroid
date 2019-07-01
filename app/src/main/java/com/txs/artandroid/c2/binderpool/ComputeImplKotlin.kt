package com.txs.artandroid.c2.binderpool

/**
 * Author: txs
 * Date: 19-6-25 下午7:13
 * Package:com.txs.artandroid.c2.binderpool
 */
class ComputeImplKotlin : ICompute.Stub {
    constructor():super(){

    }

    override fun add(a: Int, b: Int): Int {
        return a + b
    }
}