package com.fewlaps.quitnowcache;

import java.util.HashMap;

public class QNCache {

    //region Making the class testable
    Long mockedDate;

    public long now() {
        if (mockedDate == null) {
            return System.currentTimeMillis();
        } else {
            return mockedDate;
        }
    }

    public void setMockDate(long date) {
        mockedDate = date;
    }
    //endregion

    HashMap<String, Object> cache = new HashMap();

    public void set(String id, Object value, long keepAliveInSeconds) {
        cache.put(id, value);
    }

    public Object get(String id) {
        return cache.get(id);
    }


}
