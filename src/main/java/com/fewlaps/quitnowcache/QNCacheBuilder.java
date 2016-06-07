package com.fewlaps.quitnowcache;

import java.util.concurrent.TimeUnit;

public class QNCacheBuilder {
    private boolean caseSensitiveKeys = true;
    private Integer autoReleaseInSeconds = null;
    private long defaultKeepaliveInMillis = QNCache.KEEPALIVE_FOREVER;

    public QNCacheBuilder setCaseSensitiveKeys(boolean caseSensitiveKeys) {
        this.caseSensitiveKeys = caseSensitiveKeys;
        return this;
    }

    public QNCacheBuilder setAutoRelease(int units, TimeUnit timeUnit) {
        this.autoReleaseInSeconds = Math.toIntExact(timeUnit.toSeconds(units));
        return this;
    }

    public QNCacheBuilder setAutoReleaseInSeconds(Integer autoReleaseInSeconds) {
        this.autoReleaseInSeconds = autoReleaseInSeconds;
        return this;
    }

    public QNCacheBuilder setDefaultKeepalive(int units, TimeUnit timeUnit) {
        this.defaultKeepaliveInMillis = timeUnit.toMillis(units);
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