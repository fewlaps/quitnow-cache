package com.fewlaps.quitnowcache;

import java.util.concurrent.TimeUnit;

import static com.fewlaps.quitnowcache.QNCache.KEEPALIVE_FOREVER;
import static com.fewlaps.quitnowcache.QNCache.WITHOUT_AUTORELEASE;

/**
 * @deprecated Use QNCache.builder() instead.
 */
@Deprecated
public class QNCacheBuilder {
    private boolean caseSensitiveKeys = true;
    private int autoReleaseInSeconds = QNCache.WITHOUT_AUTORELEASE;
    private long defaultKeepaliveInMillis = QNCache.KEEPALIVE_FOREVER;

    public QNCacheBuilder setCaseSensitiveKeys(boolean caseSensitiveKeys) {
        this.caseSensitiveKeys = caseSensitiveKeys;
        return this;
    }

    public QNCacheBuilder setAutoRelease(int units, TimeUnit timeUnit) {
        this.autoReleaseInSeconds = Long.valueOf(timeUnit.toSeconds(units)).intValue();
        return this;
    }

    public QNCacheBuilder setAutoReleaseInSeconds(int autoReleaseInSeconds) {
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
        if (autoReleaseInSeconds < 0) {
            autoReleaseInSeconds = WITHOUT_AUTORELEASE;
        }
        if (defaultKeepaliveInMillis < 0) {
            defaultKeepaliveInMillis = KEEPALIVE_FOREVER;
        }
        return new QNCache<T>(caseSensitiveKeys, autoReleaseInSeconds, defaultKeepaliveInMillis);
    }
}