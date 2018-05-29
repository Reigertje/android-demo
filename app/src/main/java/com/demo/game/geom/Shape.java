package com.demo.game.geom;

public abstract class Shape {

    protected static final int POINT = 0;

    protected static final int CIRCLE = 1;

    protected static final int RECTANGLE = 2;

    private int type;

    private Vec2 position;

    protected Shape(int type) {
        this.type = type;
        this.position = new Vec2();
    }

    public void setPosition(Vec2 position) {
        this.position.set(position);
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public void translate(float x, float y) {
        setPosition(getX() + x, getY() + y);
    }

    public void setX(float x) {
        this.position.setX(x);
    }

    public void setY(float y) {
        this.position.setY(y);
    }

    public void setMaxX(float x) {
        this.position.setX(x - getHalfWidth());
    }

    public void setMinX(float x) {
        this.position.setX(x + getHalfWidth());
    }

    public void setMaxY(float y) {
        this.position.setY(y - getHalfHeight());
    }

    public void setMinY(float y) {
        this.position.setY(y + getHalfHeight());
    }

    public Vec2 getPosition() {
        return position;
    }

    public float getX() {
        return position.getX();
    }

    public float getY() {
        return position.getY();
    }

    public float getMaxX() {
        return position.getX() + getHalfWidth();
    }

    public float getMinX() {
        return position.getX() - getHalfWidth();
    }

    public float getMaxY() {
        return position.getY() + getHalfHeight();
    }

    public float getMinY() {
        return position.getY() - getHalfHeight();
    }

    public boolean contains(Vec2 point) {
        return contains(point.getX(), point.getY());
    }

    public abstract boolean contains(float x, float y);

    public abstract float getHalfWidth();

    public abstract float getHalfHeight();

    public boolean collides(Shape other) {
        if (type == POINT) return other.contains(getX(), getY());
        else if (other.type == POINT) return this.contains(other.getX(), other.getY());
        else if (type == RECTANGLE) {
            return other.type == RECTANGLE ?
                    collides((Rectangle)this, (Rectangle)other) :
                    collides((Rectangle)this, (Circle)other);
        } else {
            return other.type == RECTANGLE ?
                    collides((Rectangle)other, (Circle)this) :
                    collides((Circle)other, (Circle)this);
        }
    }

    private static boolean collides(Rectangle a, Rectangle b) {
        return a.getMinX() < b.getMaxX() &&
                a.getMaxX() > b.getMinX() &&
                a.getMinY() < b.getMaxY() &&
                a.getMaxY() > b.getMinY();
    }

    private static boolean collides(Rectangle a, Circle b) {
        return a.contains(b.getX(), b.getY()) ||
                b.contains(Math.max(Math.min(b.getX(), a.getMaxX()), a.getMinX()),
                        Math.max(Math.min(b.getY(), a.getMaxY()), a.getMinY()));
    }


    private static boolean collides(Circle a, Circle b) {
        return (a.getX() - b.getX()) * (a.getX() - b.getX()) + (a.getY() - b.getY()) * (a.getY() - b.getY()) < a.getRadius() * a.getRadius() + b.getRadius() * b.getRadius();
    }

}
