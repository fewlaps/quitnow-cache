package com.fewlaps.quitnowcache;

public class MockDateProvider implements DateProvider {

    private Long fakeNow;

    public void setFixed(long fakeNow) {
        this.fakeNow = fakeNow;
    }

    public void setSystem() {
        this.fakeNow = null;
    }

    @Override
    public long now() {
        return fakeNow != null ? fakeNow : DateProvider.SYSTEM.now();
    }
}
