package com.demo.android;

import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

/**
 * MotionEvent wordt door Android hergebruikt, dus is niet betrouwbaar om op te slaan voor later
 * gebruik. Vandaar deze klasse. Kloont de waarden van een event die we nodig hebben om op te slaan.
 * Natuurlijk willen we niet onnodig objecten aanmaken (Android hergebruikt zelf die MotionEvents niet
 * voor niks, dus vandaar met een pool!
 */

public class ClonedMotionEvent {

    private static final int MAX_POOL_SIZE = 20;

    private static final ClonedMotionEvent[] eventPool = new ClonedMotionEvent[MAX_POOL_SIZE];

    static {
        for (int i = 0; i < MAX_POOL_SIZE; ++i) {
            eventPool[i] = new ClonedMotionEvent();
        }
    }

    private static int poolSize = 0;

    private float x, y;

    private int action, index;

    static ClonedMotionEvent fromMotionEvent(MotionEvent motionEvent) {

        ClonedMotionEvent event = new ClonedMotionEvent();

        event.setMotionEvent(motionEvent);

        return event;

/*        synchronized (eventPool) {
            if (poolSize == MAX_POOL_SIZE - 1) return null;

            eventPool[poolSize].setMotionEvent(motionEvent);

            return eventPool[poolSize++];
        }*/
    }

    static void clear() {
        poolSize = 0;
    }

    private void setMotionEvent(MotionEvent motionEvent) {
        x = motionEvent.getX();
        y = motionEvent.getY();
        action = MotionEventCompat.getActionMasked(motionEvent);
        index = MotionEventCompat.getActionIndex(motionEvent);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getAction() {
        return action;
    }

    public int getIndex() {
        return index;
    }

}
