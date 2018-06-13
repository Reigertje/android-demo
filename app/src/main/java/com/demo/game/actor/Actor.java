package com.demo.game.actor;

import com.demo.game.geom.Shape;
import com.demo.game.geom.Vec2;
import com.demo.game.graphics.GraphicsObject;
import com.demo.game.scene.Scene;

public abstract class Actor implements Comparable<Actor> {

    private static long next_id = 1;

    private Long id;

    private Scene scene;

    private ActorGraphics graphics;

    private ActorInteraction interaction;

    private Vec2 position;

    public Actor() {
        this.scene = null;
        this.id = next_id++;
        this.position = new Vec2();
        this.graphics = new ActorGraphics(this);
        this.interaction = new ActorInteraction(this);
    }

    public Scene getScene() {
        return scene;
    }

    private void setScene(Scene scene) {
        this.scene = scene;
    }

    public Vec2 getPosition() {
        return position;
    }

    public void setPosition(Vec2 position) {
        position.set(position);
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public long getId() {
        return id;
    }

    public boolean isInScene() {
        return scene != null;
    }

    public void addToScene(Scene scene) {
        this.scene = scene;
        graphics.addToScene(getScene());
        interaction.addToScene(getScene());
    }

    public void removeFromScene() {
        graphics.removeFromScene(getScene());
        interaction.removeFromScene(getScene());
        this.scene = null;
    }

    public void step(float dt) {
        onStep(dt);
        graphics.step(dt);
        interaction.step(dt);
    }

    public abstract void onStep(float dt);

    public final void addGraphicsObject(GraphicsObject object) {
        graphics.addGraphicsObject(object);
    }

    public int getGraphicsObjectCount() {
        return graphics.count();
    }

    public final void addGraphicsObjectWithOffset(GraphicsObject object, Vec2 offset) {
        int i = graphics.count();
        graphics.addGraphicsObject(object);
        setGraphicsObjectOffset(i, offset);
    }

    public final void setGraphicsObjectOffset(int index, Vec2 offset) {
        graphics.setGraphicsObjectOffset(index, offset);
    }

    public final void setGraphicsObjectOffset(int index, float x, float y) {
        graphics.setGraphicsObjectOffset(index, x, y);
    }

    public final Vec2 getGraphicsObjectOffset(int index) {
        return graphics.getGraphicsObjectOffset(index);
    }

    public final GraphicsObject getGraphicsObject(int index) {
        return graphics.getGraphicsObject(index);
    }

    public final void setInteractionShape(Shape interactionShape) {
        interaction.setInteractionShape(interactionShape);
    }

    public final void setInteractionShapeOffset(Vec2 offset) {
        interaction.setInteractionShapeOffset(offset);
    }

    public final void setInteractionShapeOffset(float x, float y) {
        interaction.setInteractionShapeOffset(x, y);
    }

    public String toString() {
        return "Actor(" + id + ")";
    }


    @Override
    public boolean equals(Object o) {
        return (this == o || (o instanceof Actor && id.equals(((Actor)o).id)));
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int compareTo(Actor actor) {
        return id.compareTo(actor.getId());
    }
}
