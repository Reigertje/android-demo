package com.demo.game.actor;

import java.util.ArrayList;
import java.util.List;

import com.demo.game.Game;
import com.demo.game.geom.Vec2;
import com.demo.game.graphics.GraphicsObject;
import com.demo.game.scene.Scene;

class ActorGraphics {

    private Actor actor;

    private List<GraphicsObject> graphicsObjects;

    private List<Vec2> graphicsObjectOffsets;

    private boolean isFirstStep;

    public ActorGraphics(Actor actor) {
        this.actor = actor;
        this.isFirstStep = true;
        graphicsObjects = new ArrayList<>();
        graphicsObjectOffsets = new ArrayList<>();
    }

    public void addGraphicsObject(GraphicsObject object) {
        graphicsObjects.add(object);
        graphicsObjectOffsets.add(new Vec2());
    }

    public void setGraphicsObjectOffset(int index, Vec2 offset) {
        graphicsObjectOffsets.get(index).set(offset);
    }

    public void setGraphicsObjectOffset(int index, float x, float y) {
        graphicsObjectOffsets.get(index).set(x, y);
    }

    public GraphicsObject getGraphicsObject(int index) {
        return graphicsObjects.get(index);
    }

    public void removeGraphicsObject(GraphicsObject object) {
        int index = graphicsObjects.indexOf(object);
        if (index >= 0) {
            graphicsObjects.remove(index);
            graphicsObjectOffsets.remove(index);
        }
    }

    public void addToScene(Scene scene) {
        for (GraphicsObject graphicsObject : graphicsObjects) {
           scene.addGraphicsObject(graphicsObject);
        }
    }

    public void removeFromScene(Scene scene) {
        for (GraphicsObject graphicsObject : graphicsObjects) {
            scene.removeGraphicsObject(graphicsObject);
        }
    }

    public void step(float dt) {
        for (int i = 0; i < graphicsObjects.size(); ++i) {
            GraphicsObject object = graphicsObjects.get(i);

            object.lerpFromCurrentPosition();
            Vec2 offset = graphicsObjectOffsets.get(i);

            object.getPosition().set(
                    Game.toPixels(actor.getPosition().getX()) + offset.getX(),
                    Game.toPixels(actor.getPosition().getY()) + offset.getY());

            if (isFirstStep) {
                object.lerpFromCurrentPosition();
            }

            object.step(dt);
        }
        isFirstStep = false;
    }

}
