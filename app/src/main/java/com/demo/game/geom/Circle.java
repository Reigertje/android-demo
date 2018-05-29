package com.demo.game.geom;

public class Circle extends Shape {

    private float radius;

    public Circle(float radius) {
        super(Shape.CIRCLE);
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public float getHalfWidth() {
        return radius;
    }

    @Override
    public float getHalfHeight() {
        return radius;
    }

    @Override
    public boolean contains(float x, float y) {
        return (getX() - x) * (getX() - x) + (getY() - y) * (getY() - y) < radius * radius;
    }

}
