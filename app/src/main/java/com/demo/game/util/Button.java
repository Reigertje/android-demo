package com.demo.game.util;

import com.demo.game.geom.Shape;
import com.demo.game.geom.Vec2;

public class Button {

    private Shape shape;

    private boolean enabled;

    private boolean wasPressed;

    private boolean isDown;

    private boolean wasReleased;

    private int pointerIndex;

    private ClickHandler clickHandler;

    private ReleaseHandler releaseHandler;

    public Button(Shape shape) {
        this.shape = shape;
    }

    public void setClickHandler(ClickHandler handler) {
        this.clickHandler = handler;
    }

    public void setReleaseHandler(ReleaseHandler handler) {
        this.releaseHandler = handler;
    }

    public void setPosition(Vec2 position) {
        shape.setPosition(position);
    }

    public void setPosition(float x, float y) {
        shape.setPosition(x, y);
    }

    public boolean wasPressed() {
        return wasPressed;
    }

    public boolean isDown() {
        return isDown;
    }

    public boolean wasReleased() {
        return wasReleased;
    }

    public int getPointerIndex() {
        return pointerIndex;
    }

    public void click(int pointerIndex) {
        this.pointerIndex = pointerIndex;
        wasPressed = true;
        isDown = true;
        if (clickHandler != null) clickHandler.onClick();
    }

    public void release() {
        pointerIndex = -1;
        wasReleased = true;
        isDown = false;
        if (releaseHandler != null) releaseHandler.onRelease();
    }

    public boolean contains(Vec2 point) {
        return shape.contains(point);
    }

    public boolean contains(float x, float y) {
        return shape.contains(x, y);
    }

    public void step(float dt) {
        wasReleased = false;
        wasPressed = false;
    }

    public interface ClickHandler {
        void onClick();
    }

    public interface ReleaseHandler {
        void onRelease();
    }

}
