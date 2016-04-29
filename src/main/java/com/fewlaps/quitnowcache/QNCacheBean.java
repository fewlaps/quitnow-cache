package com.fewlaps.quitnowcache;

/**
 * This class is not public 'cause we'll hide the implementation of the QNCache itself
 */
class QNCacheBean<T> {

    private final long creationDate;
    private final long keepAliveInMillis;
    private final T value;

    public QNCacheBean(T value, long creationDate, long keepAliveInMillis) {
        this.creationDate = creationDate;
        this.keepAliveInMillis = keepAliveInMillis;
        this.value = value;
    }

    public boolean isAlive(long now) {
        if (QNCache.KEEPALIVE_FOREVER == keepAliveInMillis) {
            return true;
        } else {
            return creationDate + keepAliveInMillis > now;
        }
    }

    public T getValue() {
        return value;
    }
}
