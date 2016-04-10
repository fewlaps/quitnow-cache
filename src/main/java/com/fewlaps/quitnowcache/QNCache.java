package com.fewlaps.quitnowcache;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

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

        cache = new ConcurrentHashMap<>();

        startAutoReleaseServiceIfNeeded();
    }

    private void startAutoReleaseServiceIfNeeded() {
        if (autoReleaseInSeconds != null) {
            ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

            ses.scheduleAtFixedRate(this::removeTooOldValues, autoReleaseInSeconds, autoReleaseInSeconds, TimeUnit.SECONDS);
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
            cache.put(key, new QNCacheBean<>(value, now(), keepAliveInMillis));
        }
    }

    T maybeGet(String key) {
        key = getEffectiveKey(key);

        QNCacheBean<T> retrievedValue = cache.get(key);
        if (retrievedValue == null || !retrievedValue.isAlive(now())) {
            return null;
        } else {
            return retrievedValue.getValue();
        }
    }

    /**
     * Gets an element from the cache.
     */
    public Optional<T> get(String key) {
        return Optional.ofNullable(maybeGet(key));
    }

    /**
     * Gets an element from the cache.
     * Uses the supplier if not present and caches the value with the default keep alive.
     */
    public T getOrElseCache(String key, Supplier<? extends T> supplier) {
        Optional<T> optionalValue = get(key);
        if (optionalValue.isPresent()) {
            return optionalValue.get();
        } else {
            T newValue = supplier.get();
            this.set(key, newValue);
            return newValue;
        }
    }

    /**
     * Gets an element from the cache.
     * Uses the supplier if not present and caches the value with the specified keep alive.
     */
    public T getOrElseCache(String key, Supplier<? extends T> supplier, long keepAliveInMillis) {
        Optional<T> optionalValue = get(key);
        if (optionalValue.isPresent()) {
            return optionalValue.get();
        } else {
            T newValue = supplier.get();
            this.set(key, newValue, keepAliveInMillis);
            return newValue;
        }
    }

    T maybeGetAndRemoveIfDead(String key) {
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

    /**
     * Gets an element from the cache. If the element exists but it's dead,
     * it will be removed of the cache, to free memory
     */
    Optional<T> getAndRemoveIfDead(String key) {
        return Optional.ofNullable(maybeGetAndRemoveIfDead(key));
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
        return ((int) cache.values().stream()
          .filter(value -> value.isAlive(now()))
          .count());
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

        return maybeGet(key) != null;
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
