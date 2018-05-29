package com.demo.game.graphics;

import android.content.Context;
import android.opengl.GLES20;

import com.demo.game.graphics.renderer.SceneRenderer;

public abstract class Texture {

    private static int LAST_BOUND_TEXTURE_ID = -1;

    private float x0, x1, y0, y1;

    private int pixelWidth, pixelHeight;

    public abstract void bind();

    public void bindTextureId(int id) {
        if (id != LAST_BOUND_TEXTURE_ID) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id);
            SceneRenderer.textureBindCalls++;
            LAST_BOUND_TEXTURE_ID = id;
        }
    }

    public static void clearLastBound() {
        LAST_BOUND_TEXTURE_ID = -1;
    }

    public abstract void load(Context context);

    public abstract void delete();

    private void setPixelWidth(int pixelWidth) {
        this.pixelWidth = pixelWidth;
    }

    private void setPixelHeight(int pixelHeight) {
        this.pixelHeight = pixelHeight;
    }

    protected void setPixelSize(int pixelWidth, int pixelHeight) {
        setPixelWidth(pixelWidth);
        setPixelHeight(pixelHeight);
    }

    protected void setX0(float x0) {
        this.x0 = x0;
    }

    protected void setX1(float x1) {
        this.x1 = x1;
    }

    protected void setY0(float y0) {
        this.y0 = y0;
    }

    protected void setY1(float y1) {
        this.y1 = y1;
    }

    public float getX0() {
        return x0;
    }

    public float getX1() {
        return x1;
    }

    public float getY0() {
        return y0;
    }

    public float getY1() {
        return y1;
    }

    protected void setRectangle(float x0, float y0, float x1, float y1) {
        setX0(x0);
        setY0(y0);
        setX1(x1);
        setY1(y1);
    }

    public float getWidth() {
        return x1 - x0;
    }

    public float getHeight() {
        return y1- y0;
    }

    public int getPixelWidth() {
        return pixelWidth;
    }

    public int getPixelHeight() {
        return pixelHeight;
    }

}