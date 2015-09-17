package com.fewlaps.quitnowcache;

/**
 * This class is not public 'cause we'll hide the implementation of the QNCache itself
 */
class QNCacheBean {
    private long creationDate;
    private long keepAlive;
    private Object value;

    public QNCacheBean(Object value, long creationDate, long keepAlive) {
        this.creationDate = creationDate;
        this.keepAlive = keepAlive;
        this.value = value;
    }

    public boolean isAlive(long now) {
        return creationDate + keepAlive > now;
    }
}
