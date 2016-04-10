package com.fewlaps.quitnowcache;

interface DateProvider {

    long now();

    DateProvider SYSTEM = System::currentTimeMillis;
}
