package com.demo.game.graphics;

import android.util.SparseArray;

import com.demo.game.geom.Vec2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GraphicsScene {

    public static final int BELOW_WATER_LAYER = 0;
    public static final int ABOVE_WATER_LAYER = 1;

    private List<GraphicsObject> objects;

    private SparseArray<List<GraphicsObject>> layers;

    private Vec2 lerpCameraPosition;

    private Vec2 cameraPosition;

    private Vec2 cameraOffset;

    public GraphicsScene() {
        this.objects = new ArrayList<>();
        this.cameraPosition = new Vec2();
        this.lerpCameraPosition = new Vec2();
        this.cameraOffset = new Vec2();
        this.layers = new SparseArray<>();
    }

    public void addObject(GraphicsObject object) {
        objects.add(object);
    }

    public void removeObject(GraphicsObject object) {
        objects.remove(object);
    }

    public Vec2 getCameraPosition() {
        return cameraPosition;
    }

    public void setCameraPosition(float x, float y) {
        cameraPosition.set(x, y);
    }

    public void setCameraPosition(Vec2 position) {
        cameraPosition.set(position);
    }

    public Vec2 getCameraOffset() {
        return cameraOffset;
    }

    public void setCameraOffset(float x, float y) {
        this.cameraOffset.set(x, y);
    }

    public void setCameraOffset(Vec2 offset) {
        this.cameraOffset.set(offset);
    }

    public void lerpCameraFromCurrentPosition() {
        lerpCameraPosition.set(cameraPosition);
    }

    public Vec2 getLerpCameraPosition() {
        return lerpCameraPosition;
    }

    public List<GraphicsObject> getLayer(int layer) {
        List<GraphicsObject> layerList = layers.get(layer);
        if (layerList == null) {
            layers.put(layer, layerList = new ArrayList<>());
        }
        return layerList;
    }

    private void addObjectToLayer(GraphicsObject object) {
        List<GraphicsObject> toAdd = getLayer(object.getLayer());
        toAdd.add(object);
    }

    private void clearBuffers() {
        for(int i = 0; i < layers.size(); i++) {
            layers.get(layers.keyAt(i)).clear();
        }
    }

    private static int compareGraphicsObjects(GraphicsObject a, GraphicsObject b) {
        return a.getDepth() == b.getDepth() ? 0 : a.getDepth() > b.getDepth() ? -1 : 1;
    }

    private void sortBuffers() {
        for(int i = 0; i < layers.size(); i++) {
            Collections.sort(layers.get(layers.keyAt(i)), GraphicsScene::compareGraphicsObjects);
        }
    }

    private void constructBuffers() {
        for (GraphicsObject object : objects) {
            addObjectToLayer(object);
        }
    }

    public void step() {
        clearBuffers();
        constructBuffers();
        sortBuffers();
    }

    public static abstract class Renderer {

        public abstract void render(GraphicsScene scene, float lerp);

    }

}
