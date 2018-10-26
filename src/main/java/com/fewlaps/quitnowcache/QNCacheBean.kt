package com.fewlaps.quitnowcache

/**
 * This class is not public 'cause we'll hide the implementation of the QNCache itself
 */
internal class QNCacheBean<T>(
        val value: T,
        private val creationDate: Long,
        private val keepAliveInMillis: Long) {

    fun isAlive(now: Long): Boolean {
        return when (keepAliveInMillis) {
            QNCache.KEEPALIVE_FOREVER -> true
            else -> creationDate + keepAliveInMillis > now
        }
    }
}
