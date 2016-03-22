package com.fewlaps.quitnowcache;

interface DateProvider {

    DateProvider SYSTEM = new DateProvider() {

        @Override
        public long now() {
            return System.currentTimeMillis();
        }
    };

    long now();
}
