package com.demo.game.interaction;

import com.demo.game.geom.Vec2;
import com.demo.game.actor.Actor;
import com.demo.game.geom.Shape;

public class InteractionObject {

    private Actor actor;

    private Shape shape;

    private boolean canInteract;

    public InteractionObject(Actor actor, Shape shape) {
        this.actor = actor;
        this.shape = shape;
    }

    public Vec2 getPosition() {
        return shape.getPosition();
    }

    public void setPosition(Vec2 position) {
        this.getPosition().set(position);
    }

    public void setPosition(float x, float y) {
        this.getPosition().set(x, y);
    }

    public Actor getActor() {
        return actor;
    }

    public boolean interactsWith(InteractionObject other) {
        return shape.collides(other.shape);
    }

    public void setCanInteract(boolean canInteract) {
        this.canInteract = canInteract;
    }

    public boolean canInteract() {
        return canInteract;
    }

    public long getId() {
        return actor.getId();
    }

}
