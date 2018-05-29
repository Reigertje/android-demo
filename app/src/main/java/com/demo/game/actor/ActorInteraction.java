package com.demo.game.actor;

import com.demo.game.geom.Shape;
import com.demo.game.geom.Vec2;
import com.demo.game.interaction.InteractionObject;
import com.demo.game.scene.Scene;

public class ActorInteraction {

    private Actor actor;

    private InteractionObject interactionObject;

    private Vec2 interactionShapeOffset;

    public ActorInteraction(Actor actor) {
        this.actor = actor;
        this.interactionShapeOffset = new Vec2();
    }

    public void setInteractionShape(Shape interactionShape) {
        this.interactionObject = interactionShape == null ? null : new InteractionObject(actor, interactionShape);
    }

    public void setInteractionShapeOffset(Vec2 offset) {
        this.interactionShapeOffset.set(offset);
    }

    public void setInteractionShapeOffset(float x, float y) {
        this.interactionShapeOffset.set(x, y);
    }

    public void addToScene(Scene scene) {
        if (interactionObject != null) {
            scene.addInteractionObject(interactionObject);
        }
    }

    public void removeFromScene(Scene scene) {
        if (interactionObject != null) {
            scene.removeInteractionObject(interactionObject);
        }
    }

    public Actor getActor() {
        return actor;
    }

    public void step(float dt) {
        if (interactionObject != null) {
            interactionObject.getPosition().setSum(actor.getPosition(), interactionShapeOffset);
        }
    }

}
