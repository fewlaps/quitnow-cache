package com.fewlaps.quitnowcache;

/**
 * This class is not public 'cause we'll hide the implementation of the QNCache itself
 */
class QNCacheBean {

    public static final int FOREVER_KEEPALIVE = 0;

    private long creationDate;
    private long keepAlive;
    private Object value;

    public QNCacheBean(Object value, long creationDate, long keepAlive) {
        this.creationDate = creationDate;
        this.keepAlive = keepAlive;
        this.value = value;
    }

    public boolean isAlive(long now) {
        if (FOREVER_KEEPALIVE == keepAlive) {
            return true;
        } else {
            return creationDate + keepAlive > now;
        }
    }

    public Object getValue() {
        return value;
    }
}
