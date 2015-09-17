package com.fewlaps.quitnowcache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class QNCache {

    //region Making the class testable
    private Long mockedDate;

    public long now() {
        if (mockedDate == null) {
            return System.currentTimeMillis();
        } else {
            return mockedDate;
        }
    }

    @Deprecated
    public void setMockDate(long date) {
        mockedDate = date;
    }
    //endregion

    HashMap<String, QNCacheBean> cache = new HashMap();

    public void set(String key, Object value, long keepAliveInSeconds) {
        cache.put(key, new QNCacheBean(value, now(), keepAliveInSeconds));
    }

    public Object get(String key) {
        QNCacheBean retrievedValue = cache.get(key);
        if (retrievedValue == null || !retrievedValue.isAlive(now())) {
            return null;
        } else {
            return cache.get(key).getValue();
        }
    }

    public Object getAndRemoveIfDead(String key) {
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
        cache.remove(key);
    }

    public void removeAll() {
        cache.clear();
    }

    public void removeTooOldValues() {
        Iterator it = cache.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            QNCacheBean bean = (QNCacheBean) pair.getValue();
            if (!bean.isAlive(now())) {
                it.remove();
            }
        }
    }

    public int size() {
        return sizeCountingOnlyAliveElements();
    }

    public int sizeCountingOnlyAliveElements() {
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

    public int sizeCountingDeadAndAliveElements() {
        return cache.size();
    }

    public boolean isEmpty() {
        return sizeCountingOnlyAliveElements() == 0;
    }
}
