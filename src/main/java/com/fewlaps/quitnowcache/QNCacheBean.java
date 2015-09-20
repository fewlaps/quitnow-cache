package com.fewlaps.quitnowcache;

/**
 * This class is not public 'cause we'll hide the implementation of the QNCache itself
 */
class QNCacheBean {

    public static final int FOREVER_KEEPALIVE = 0;

    private long creationDate;
    private long keepAliveInMillis;
    private Object value;

    public QNCacheBean(Object value, long creationDate, long keepAliveInMillis) {
        this.creationDate = creationDate;
        this.keepAliveInMillis = keepAliveInMillis;
        this.value = value;
    }

    public boolean isAlive(long now) {
        if (FOREVER_KEEPALIVE == keepAliveInMillis) {
            return true;
        } else {
            return creationDate + keepAliveInMillis > now;
        }
    }

    public Object getValue() {
        return value;
    }
}
