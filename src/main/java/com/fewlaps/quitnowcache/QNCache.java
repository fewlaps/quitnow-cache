package com.fewlaps.quitnowcache;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class QNCache<T> {

    public static final long KEEPALIVE_FOREVER = 0;

    private final ConcurrentHashMap<String, QNCacheBean<T>> cache;
    private boolean caseSensitiveKeys = true;
    private Integer autoReleaseInSeconds;
    private Long defaultKeepaliveInMillis;
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
                    purge();
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

    public void set(String key, T value) {
        if (defaultKeepaliveInMillis != null) {
            set(key, value, defaultKeepaliveInMillis);
        } else {
            set(key, value, KEEPALIVE_FOREVER);
        }
    }

    public void set(String key, T value, long keepAliveInMillis) {
        String effectiveKey = getEffectiveKey(key);

        if (keepAliveInMillis >= 0) {
            cache.put(effectiveKey, new QNCacheBean<T>(value, now(), keepAliveInMillis));
        }
    }

    public void set(String key, T value, long keepAliveUnits, TimeUnit timeUnit) {
        set(key, value, timeUnit.toMillis(keepAliveUnits));
    }

    /**
     * Gets an element from the cache.
     */
    public T get(String key) {
        String effectiveKey = getEffectiveKey(key);

        QNCacheBean<T> retrievedValue = cache.get(effectiveKey);
        if (retrievedValue == null || !retrievedValue.isAlive(now())) {
            return null;
        } else {
            return retrievedValue.getValue();
        }
    }

    /**
     * Gets an element from the cache. If the element exists but it's dead,
     * it will be removed from the cache to free memory. It could call
     * an internal synchronized method, so avoid calling this method if
     * it's not needed.
     */
    public T getAndPurgeIfDead(String key) {
        String effectiveKey = getEffectiveKey(key);

        QNCacheBean<T> retrievedValue = cache.get(effectiveKey);
        if (retrievedValue == null) {
            return null;
        } else if (retrievedValue.isAlive(now())) {
            return retrievedValue.getValue();
        } else {
            cache.remove(effectiveKey);
            return null;
        }
    }

    public void remove(String key) {
        String effectiveKey = getEffectiveKey(key);
        cache.remove(effectiveKey);
    }

    /**
     * Removes all the elements of the cache, ignoring if they're dead or alive
     */
    public void clear() {
        cache.clear();
    }

    public List<String> keySetDeadAndAlive() {
        return Collections.list(cache.keys());
    }

    public List<String> keySetAlive() {
        List<String> aliveKeys = new ArrayList<String>();

        for (String key : keySetDeadAndAlive()) {
            if (isKeyAlive(key)) {
                aliveKeys.add(key);
            }
        }

        return aliveKeys;
    }

    public List<String> keySetDead() {
        List<String> aliveKeys = new ArrayList<String>();

        for (String key : keySetDeadAndAlive()) {
            if (isKeyDead(key)) {
                aliveKeys.add(key);
            }
        }

        return aliveKeys;
    }

    public List<String> keySetStartingWith(String keyStartingWith) {
        List<String> filteredKeys = new ArrayList<String>();
        String effectiveKeyStartingWith = getEffectiveKey(keyStartingWith);

        for (String key : keySetDeadAndAlive()) {
            if (key.startsWith(effectiveKeyStartingWith)) {
                filteredKeys.add(key);
            }
        }

        return filteredKeys;
    }

    public List<String> keySetAliveStartingWith(String keyStartingWith) {
        List<String> aliveFilteredKeys = new ArrayList<String>();

        for (String key : keySetStartingWith(keyStartingWith)) {
            if (isKeyAlive(key)) {
                aliveFilteredKeys.add(key);
            }
        }

        return aliveFilteredKeys;
    }

    public boolean isKeyAlive(String key) {
        String effectiveKey = key;
        return cache.get(effectiveKey).isAlive(now());
    }

    public boolean isKeyDead(String key) {
        return !isKeyAlive(key);
    }

    /**
     * Counts how much alive elements are living in the cache
     */
    public int size() {
        return sizeAliveElements();
    }

    /**
     * Counts how much alive elements are living in the cache
     */
    public int sizeAliveElements() {
        int size = 0;

        for (QNCacheBean<T> cacheValue : cache.values()) {
            if (cacheValue.isAlive(now())) {
                size++;
            }
        }
        return size;
    }

    /**
     * Counts how much dead elements exist in the cache
     */
    public int sizeDeadElements() {
        return cache.size() - sizeAliveElements();
    }

    /**
     * Counts how much elements are living in the cache, ignoring if they are dead or alive
     */
    public int sizeDeadAndAliveElements() {
        return cache.size();
    }

    /**
     * The common isEmpty() method, but only looking for alive elements
     */
    public boolean isEmpty() {
        return sizeAliveElements() == 0;
    }

    /**
     * The common contains() method
     */
    public boolean contains(String key) {
        String effectiveKey = getEffectiveKey(key);
        return get(effectiveKey) != null;
    }

    /**
     * Removes the dead elements of the cache, to free memory
     */
    void purge() {
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
