package com.fewlaps.quitnowcache;

public class QNCacheBuilder {
    private boolean caseSensitiveKeys = true;
    private boolean threadSafe = true;

    public QNCacheBuilder setCaseSensitiveKeys(boolean caseSensitiveKeys) {
        this.caseSensitiveKeys = caseSensitiveKeys;
        return this;
    }

    public QNCacheBuilder setThreadSafe(boolean threadSafe) {
        this.threadSafe = threadSafe;
        return this;
    }

    public QNCache createQNCache() {
        return new QNCache(caseSensitiveKeys, threadSafe);
    }
}