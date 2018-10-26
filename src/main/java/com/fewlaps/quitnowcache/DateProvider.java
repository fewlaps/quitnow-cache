package com.fewlaps.quitnowcache;

public interface DateProvider {

    DateProvider SYSTEM = new DateProvider() {
        @Override
        public long now() {
            return System.currentTimeMillis();
        }
    };

    long now();
}
