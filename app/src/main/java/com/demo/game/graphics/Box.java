package com.demo.game.graphics;

import com.demo.game.geom.Vec2;

/**
 * Created by brian on 15-2-18.
 */

public class Box {

    float minx, miny, maxx, maxy;

    Vec2 position;

    public Box(float minx, float miny, float maxx, float maxy) {
        this.minx = minx;
        this.maxx = maxx;
        this.miny = miny;
        this.maxy = maxy;
        this.position = new Vec2();
    }

    public float getMinX() {
        return minx + position.x;
    }

    public float getMaxX() {
        return maxx + position.x;
    }

    public float getMinY() {
        return miny + position.y;
    }

    public float getMaxY() {
        return maxy + position.y;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

}
