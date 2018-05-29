package com.demo.game.graphics;


public class Color {

    public float[] data;

    public Color(float r, float g, float b, float a) {
        data = new float[]{r, g, b, a};
    }

    public Color(float r, float g, float b) {
        this(r, g, b, 1.0f);
    }

    public Color() {
        this(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void set(Color other) {
        set(other.data[0], other.data[1], other.data[2], other.data[3]);
    }

    public void set(float r, float g, float b, float a) {
        data[0] = r;
        data[1] = g;
        data[2] = b;
        data[3] = a;
    }

    public void setRed(float r) {
        data[0] = r;
    }

    public void setGreen(float g) {
        data[1] = g;
    }

    public void setBlue(float b) {
        data[2] = b;
    }

    public void setAlpha(float a) {
        data[3] = a;
    }

    public float getRed() {
        return data[0];
    }

    public float getGreen() {
        return data[1];
    }

    public float getBlue() {
        return data[2];
    }

    public float getAlpha() {
        return data[3];
    }
}
