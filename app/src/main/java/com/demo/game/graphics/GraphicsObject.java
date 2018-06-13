package com.demo.game.graphics;

import com.demo.game.Game;
import com.demo.game.geom.Mat4x4;
import com.demo.game.geom.Vec2;

import java.nio.FloatBuffer;

import com.demo.game.util.BufferUtil;

import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;

public abstract class GraphicsObject {

    // Onderstaande matrices worden elke step opnieuw berekend,
    // vandaar static final, dan hoeft niet elke graphicsobject ze te hebben
    private static final Mat4x4 MODEL = new Mat4x4();
    private static final Mat4x4 VIEW = new Mat4x4();
    private static final Mat4x4 MODEL_VIEW = new Mat4x4();
    private static final Mat4x4 MODEL_VIEW_PROJECTION = new Mat4x4();
    
    private Box box;

    private Vec2 position;

    private Vec2 parallaxOffset;
    private Vec2 parallaxFactor;

    private FloatBuffer modelViewProjectionBuffer;

    private Vec2 lerpPosition;

    private boolean lerpEnabled;

    private boolean visible;

    private float depth;

    private int layer;

    private Color multiplyColor;

    private Color mixColor;

    // TODO Remove
    public boolean debug = false;

    public GraphicsObject() {
        position = new Vec2();
        lerpPosition = new Vec2();
        parallaxFactor = new Vec2(1.0f, 1.0f);
        parallaxOffset = new Vec2();
        lerpEnabled = true;
        visible = true;
        depth = 0;
        layer = 0;
        modelViewProjectionBuffer = BufferUtil.getFloatBuffer(16);

        this.multiplyColor = new Color();
        this.mixColor = new Color(0, 0, 0, 0);
    }

    public void setPosition(Vec2 position) {
        this.position.set(position);
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public Vec2 getPosition() {
        return position;
    }

    public void setParallaxOffset(Vec2 parallaxOffset) {
        this.parallaxOffset.set(parallaxOffset);
    }

    public void setParallaxOffset(float x, float y) {
        this.parallaxOffset.set(x, y);
    }

    public Vec2 getParallaxOffset() {
        return parallaxOffset;
    }

    public void setParallaxFactor(Vec2 parallaxFactor) {
        this.parallaxFactor.set(parallaxFactor);
    }

    public void setParallaxFactor(float x, float y) {
        this.parallaxFactor.set(x, y);
    }

    public Vec2 getParallaxFactor() {
        return parallaxFactor;
    }

    public Vec2 getLerpPosition() {
        return lerpPosition;
    }

    public void setLerpEnabled(boolean lerpEnabled) {
        this.lerpEnabled = lerpEnabled;
    }

    public boolean isLerpEnabled() {
        return lerpEnabled;
    }

    public void lerpFromCurrentPosition() {
        lerpPosition.set(position);
    }

    protected float lerp(float t0, float t1, float lerp) {
        return lerp * t1 + (1.0f - lerp) * t0;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public void setMultiplyColor(Color color) {
        multiplyColor.set(color);
    }

    public void setMultiplyColor(float r, float g, float b, float a) {
        multiplyColor.set(r, g, b, a);
    }

    public void setMixColor(Color color) {
        mixColor.set(color);
    }

    public void setMixColor(float r, float g, float b, float a) {
        mixColor.set(r, g, b, a);
    }

    public void setBox(Box box) {
        this.box = box;
    }

    protected boolean submitMVP(GraphicsScene scene, Shader shader, Mat4x4 projection, float lerp) {
        MODEL.setIdentity();
        VIEW.setIdentity();

        float tx, ty;

        if (lerpEnabled) {
            tx = lerp(lerpPosition.getX(), position.getX(), lerp);
            ty = lerp(lerpPosition.getY(), position.getY(), lerp);
        } else {
            tx = position.getX();
            ty = position.getY();
        }

        MODEL.setTranslate(tx, ty, 0);


        float cameraX = -lerp(scene.getLerpCameraPosition().getX(), scene.getCameraPosition().getX(), lerp);
        float cameraY = -lerp(scene.getLerpCameraPosition().getY(), scene.getCameraPosition().getY(), lerp);

        float ctx = (cameraX + scene.getCameraOffset().getX()) * parallaxFactor.getX() + parallaxOffset.getX();
        float cty = (cameraY + scene.getCameraOffset().getY()) * parallaxFactor.getY() + parallaxOffset.getY();

       if (box != null) {
            box.setPosition(tx + ctx, ty + cty);
            if (box.getMaxX() < 0 || box.getMinX() > Game.VIEW_WIDTH || box.getMaxY() < 0 || box.getMinY() > Game.VIEW_HEIGHT) {
                return false;
            }
        }

        VIEW.setTranslate(ctx, cty,0);

        MODEL_VIEW.setMultiply(VIEW, MODEL);
        MODEL_VIEW_PROJECTION.setMultiply(projection, MODEL_VIEW);

        modelViewProjectionBuffer.position(0);
        modelViewProjectionBuffer.put(MODEL_VIEW_PROJECTION.data());
        modelViewProjectionBuffer.position(0);
        glUniformMatrix4fv(shader.getUniformHandler(Shader.MVP), 1, false, modelViewProjectionBuffer);
        return true;
    }

    protected void submitColors(Shader shader) {
        glUniform4fv(shader.getUniformHandler(Shader.MIX_COLOR), 1, mixColor.data, 0);
        glUniform4fv(shader.getUniformHandler(Shader.MULTIPLY_COLOR), 1, multiplyColor.data, 0);
    }

    public abstract void draw(GraphicsScene scene, Mat4x4 projection, Shader shader, float lerp);

    public void step(float dt) {

    }

}
