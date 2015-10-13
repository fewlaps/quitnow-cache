package com.fewlaps.quitnowcache;

public interface DateProvider {

    long now();

    DateProvider SYSTEM = new DateProvider() {

        @Override
        public long now() {
            return System.currentTimeMillis();
        }
    };
}
