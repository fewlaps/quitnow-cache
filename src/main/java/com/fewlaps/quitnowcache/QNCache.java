package com.fewlaps.quitnowcache;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class QNCache<T> {

    public static final long KEEPALIVE_FOREVER = 0;

    private boolean caseSensitiveKeys = true;
    private Integer autoReleaseInSeconds = null;
    private Long defaultKeepaliveInMillis = null;
    private DateProvider dateProvider = DateProvider.SYSTEM;

    public QNCache(boolean caseSensitiveKeys, Integer autoReleaseInSeconds, Long defaultKeepaliveInMillis) {
        this.caseSensitiveKeys = caseSensitiveKeys;
        if (autoReleaseInSeconds != null && autoReleaseInSeconds > 0) {
            this.autoReleaseInSeconds = autoReleaseInSeconds;
        }
        if (defaultKeepaliveInMillis != null && defaultKeepaliveInMillis > 0) {
            this.defaultKeepaliveInMillis = defaultKeepaliveInMillis;
        }

        cache = new ConcurrentHashMap<String, QNCacheBean<T>>();

        startAutoReleaseServiceIfNeeded();
    }

    private void startAutoReleaseServiceIfNeeded() {
        if (autoReleaseInSeconds != null) {
            ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

            ses.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    removeTooOldValues();
                }
            }, autoReleaseInSeconds, autoReleaseInSeconds, TimeUnit.SECONDS);
        }
    }

    boolean isCaseSensitiveKeys() {
        return caseSensitiveKeys;
    }

    Integer getAutoReleaseInSeconds() {
        return autoReleaseInSeconds;
    }

    Long getDefaultKeepaliveInMillis() {
        return defaultKeepaliveInMillis;
    }

    private long now() {
        return dateProvider.now();
    }

    protected void setDateProvider(DateProvider dateProvider) {
        this.dateProvider = dateProvider;
    }

    private ConcurrentHashMap<String, QNCacheBean<T>> cache;

    public void set(String key, T value) {
        if (defaultKeepaliveInMillis != null) {
            set(key, value, defaultKeepaliveInMillis);
        } else {
            set(key, value, KEEPALIVE_FOREVER);
        }
    }

    public void set(String key, T value, long keepAliveInMillis) {
        key = getEffectiveKey(key);

        if (keepAliveInMillis >= 0) {
            cache.put(key, new QNCacheBean<T>(value, now(), keepAliveInMillis));
        }
    }

    /**
     * Gets an element from the cache.
     */
    public T get(String key) {
        key = getEffectiveKey(key);

        QNCacheBean<T> retrievedValue = cache.get(key);
        if (retrievedValue == null || !retrievedValue.isAlive(now())) {
            return null;
        } else {
            return retrievedValue.getValue();
        }
    }

    public Optional<T> getOptional(String key) {
        return Optional.ofNullable(get(key));
    }

    /**
     * Gets an element from the cache. If the element exists but it's dead,
     * it will be removed of the cache, to free memory
     */
    T getAndRemoveIfDead(String key) {
        key = getEffectiveKey(key);

        QNCacheBean<T> retrievedValue = cache.get(key);
        if (retrievedValue == null) {
            return null;
        } else if (retrievedValue.isAlive(now())) {
            return retrievedValue.getValue();
        } else {
            cache.remove(key);
            return null;
        }
    }

    Optional<T> getOptionalAndRemoveIfDead(String key) {
        return Optional.ofNullable(getAndRemoveIfDead(key));
    }

    public void remove(String key) {
        key = getEffectiveKey(key);

        cache.remove(key);
    }

    /**
     * Removes all the elements of the cache, ignoring if they're dead or alive
     */
    public void removeAll() {
        cache.clear();
    }

    /**
     * Removes the dead elements of the cache, to free memory
     */
    void removeTooOldValues() {
        Iterator<Map.Entry<String, QNCacheBean<T>>> it = cache.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, QNCacheBean<T>> entry = it.next();
            QNCacheBean<T> bean = entry.getValue();
            if (!bean.isAlive(now())) {
                it.remove();
            }
        }
    }

    /**
     * Counts how much alive elements are living in the cache
     */
    public int size() {
        return sizeCountingOnlyAliveElements();
    }

    /**
     * Counts how much alive elements are living in the cache
     */
    int sizeCountingOnlyAliveElements() {
        int size = 0;

        for(QNCacheBean<T> cacheValue: cache.values()) {
            if (cacheValue.isAlive(now())) {
                size++;
            }
        }
        return size;
    }

    /**
     * Counts how much elements are living in the cache, ignoring if they are dead or alive
     */
    int sizeCountingDeadAndAliveElements() {
        return cache.size();
    }

    /**
     * The common isEmpty() method, but only looking for alive elements
     */
    public boolean isEmpty() {
        return sizeCountingOnlyAliveElements() == 0;
    }

    public boolean contains(String key) {
        key = getEffectiveKey(key);

        return get(key) != null;
    }

    /**
     * If caseSensitiveKeys is false, it returns a key in lowercase. It will be
     * the key of all stored values, so the cache will be totally caseinsensitive
     */
    private String getEffectiveKey(String key) {
        if (!caseSensitiveKeys) {
            return key.toLowerCase();
        }
        return key;
    }
}
