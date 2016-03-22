package com.fewlaps.quitnowcache;

public class QNCacheBuilder {
    private boolean caseSensitiveKeys = true;
    private Integer autoReleaseInSeconds;
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

    public <T> QNCache<T> createQNCache() {
        return new QNCache<T>(caseSensitiveKeys, autoReleaseInSeconds, defaultKeepaliveInMillis);
    }
}