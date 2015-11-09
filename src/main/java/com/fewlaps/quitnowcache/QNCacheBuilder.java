package com.fewlaps.quitnowcache;

public class QNCacheBuilder {
    private boolean caseSensitiveKeys = true;
    private Integer autoReleaseInSeconds = null;
    private long defaultKeepaliveInMillis = QNCache.KEEPALIVE_FOREVER;

    public QNCacheBuilder setCaseSensitiveKeys(boolean caseSensitiveKeys) {
        this.caseSensitiveKeys = caseSensitiveKeys;
        return this;
    }

    public QNCacheBuilder setAutoReleaseInSeconds(Integer autoReleaseInSeconds) {
        this.autoReleaseInSeconds = autoReleaseInSeconds;
        return this;
    }

    public QNCacheBuilder setDefaultKeepaliveInMillis(long defaultKeepaliveInMillis) {
        this.defaultKeepaliveInMillis = defaultKeepaliveInMillis;
        return this;
    }

    public QNCache createQNCache() {
        return new QNCache(caseSensitiveKeys, autoReleaseInSeconds, defaultKeepaliveInMillis);
    }
}