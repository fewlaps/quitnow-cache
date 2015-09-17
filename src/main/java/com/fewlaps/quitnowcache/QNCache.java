package com.fewlaps.quitnowcache;

import java.util.HashMap;

public class QNCache {

    HashMap<String, Object> cache = new HashMap();

    public void set(String id, Object value, long keepAliveInSeconds) {
        cache.put(id, value);
    }

    public Object get(String id) {
        return cache.get(id);
    }
}
