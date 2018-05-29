package com.demo.game.geom;

public class Rectangle extends Shape {

    private float halfWidth;

    private float halfHeight;

    public Rectangle(float halfWidth, float halfHeight) {
        super(RECTANGLE);
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
    }

    @Override
    public float getHalfWidth() {
        return halfWidth;
    }

    @Override
    public float getHalfHeight() {
        return halfHeight;
    }

    @Override
    public boolean contains(float x, float y) {
        return getMinX() < x && getMaxX() > x &&
                getMinY() < y && getMaxY() > y;
    }

}
