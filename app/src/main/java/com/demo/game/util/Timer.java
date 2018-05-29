package com.demo.game.util;

public class Timer {

    private long nanotime;

    public Timer() {
        reset();
    }

    public void reset() {
        nanotime = System.nanoTime();
    }

    public long getElapsed() {
        return (System.nanoTime() - nanotime) / 1000000;
    }

}
