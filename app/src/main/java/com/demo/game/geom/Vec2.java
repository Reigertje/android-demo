package com.demo.game.geom;

public class Vec2 {

    public float x;

    public float y;

    public Vec2() {
        this.x = 0;
        this.y = 0;
    }

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vec2 other) {
        this.x = other.x;
        this.y = other.y;
    }

    public float length() {
        return (float)Math.sqrt(squaredLength());
    }

    public float squaredLength() {
        return x * x + y * y;
    }

    public float dot(Vec2 other) {
        return x * other.x + y * other.y;
    }

    public Vec2 sub(Vec2 other) {
        return new Vec2(x - other.x, y - other.y);
    }

    public void setSum(Vec2 a, Vec2 b) {
        this.x = a.x + b.x;
        this.y = a.y + b.y;
    }

    public void setMultiply(Vec2 a, float f) {
        this.x = a.x * f;
        this.y = a.y * y;
    }

    public static float dot(Vec2 a, Vec2 b) {
        return a.x * b.x + a.y * b.y;
    }

    public static Vec2 normalize(Vec2 a) {
        float l = a.length();

        if (l == 0.0f) return new Vec2();

        return new Vec2(a.x / l, a.y / l);
    }

    public static Vec2 perpL(Vec2 a) {
        return new Vec2(-a.y, a.x);
    }

    public static Vec2 perpR(Vec2 a) {
        return new Vec2(a.y, -a.x);
    }


    public Vec2 copy() {
        return new Vec2(x, y);
    }

    public String toString() {
        return "(" + x + ", " + y  + ")";
    }

}
