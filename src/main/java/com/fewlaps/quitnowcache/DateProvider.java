package com.fewlaps.quitnowcache;

interface DateProvider {

    long now();

    DateProvider SYSTEM = new DateProvider() {

        @Override
        public long now() {
            return System.currentTimeMillis();
        }
    };
}
