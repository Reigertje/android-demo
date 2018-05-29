package com.demo.game.geom;

public class Point extends Shape {

    public Point() {
        super(POINT);
    }

    @Override
    public float getHalfWidth() {
        return 0;
    }

    @Override
    public float getHalfHeight() {
        return 0;
    }

    @Override
    public boolean contains(float x, float y) {
        return x == getX() && y == getY();
    }

}
