package com.fewlaps.quitnowcache;

public class QNCacheBuilder {
    private boolean caseSensitiveKeys = true;

    public QNCacheBuilder setCaseSensitiveKeys(boolean caseSensitiveKeys) {
        this.caseSensitiveKeys = caseSensitiveKeys;
        return this;
    }

    public QNCache createQNCache() {
        return new QNCache(caseSensitiveKeys);
    }
}