package com.fewlaps.quitnowcache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class QNCache {

    private boolean caseSensitiveKeys = true;
    private Integer autoReleaseInSeconds = null;

    public QNCache(boolean caseSensitiveKeys, Integer autoReleaseInSeconds) {
        this.caseSensitiveKeys = caseSensitiveKeys;
        this.autoReleaseInSeconds = autoReleaseInSeconds;

        startAutoReleaseServiceIfNeeded(autoReleaseInSeconds);
    }

    private void startAutoReleaseServiceIfNeeded(Integer autoReleaseInSeconds) {
        if (autoReleaseInSeconds != null && autoReleaseInSeconds > 0) {
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

    //region Making the class testable
    private Long mockedDate;

    private long now() {
        if (mockedDate == null) {
            return System.currentTimeMillis();
        } else {
            return mockedDate;
        }
    }

    @Deprecated
    void setMockDate(long date) {
        mockedDate = date;
    }
    //endregion

    private HashMap<String, QNCacheBean> cache = new HashMap();

    public void set(String key, Object value, long keepAliveInMillis) {
        key = getEffectiveKey(key);

        if (keepAliveInMillis >= 0) {
            cache.put(key, new QNCacheBean(value, now(), keepAliveInMillis));
        }
    }

    /**
     * Gets an element from the cache.
     */
    public Object get(String key) {
        key = getEffectiveKey(key);

        QNCacheBean retrievedValue = cache.get(key);
        if (retrievedValue == null || !retrievedValue.isAlive(now())) {
            return null;
        } else {
            return retrievedValue.getValue();
        }
    }

    /**
     * Gets an element from the cache. If the element exists but it's dead,
     * it will be removed of the cache, to free memory
     */
    Object getAndRemoveIfDead(String key) {
        key = getEffectiveKey(key);

        QNCacheBean retrievedValue = cache.get(key);
        if (retrievedValue == null) {
            return null;
        } else if (retrievedValue.isAlive(now())) {
            return cache.get(key).getValue();
        } else {
            cache.remove(key);
            return null;
        }
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
        Iterator it = cache.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            QNCacheBean bean = (QNCacheBean) pair.getValue();
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
        Iterator it = cache.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            QNCacheBean bean = (QNCacheBean) pair.getValue();
            if (bean.isAlive(now())) {
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
