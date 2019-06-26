package com.txs.artandroid.c2.binderpool

import kotlin.experimental.xor

/**
 * Author: txs
 * Date: 19-6-25 下午7:03
 * Package:com.txs.artandroid.c2.binderpool
 */
 class SecurityCenterImplKotlin : ISecurityCenter.Stub(){
    val SECREF_CODE = '^'
    override fun encrypt(content: String?): String {
        val chars = content?.toByteArray()
        for (i in 0 until chars!!.size) {
            chars[i] = chars[i] xor SECREF_CODE.toByte()
        }
        return String(chars)
    }

    override fun decrypt(password: String?): String {
        return encrypt(password)
    }
}