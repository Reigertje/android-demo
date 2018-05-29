package com.demo.game.actor.actors;

import com.demo.game.Game;
import com.demo.game.actor.Actor;
import com.demo.game.graphics.SpriteGraphicsObject;
import com.demo.game.graphics.Texture;

public class StaticImageActor extends Actor {

    private Texture texture;

    public StaticImageActor(String textureKey) {
        super();
        texture = Game.getTexture(textureKey);

        SpriteGraphicsObject gobj = new SpriteGraphicsObject(texture);

        gobj.getVertices().bufferToVBO();
        addGraphicsObject(gobj);
    }

    public StaticImageActor(String textureKey, float width, float height) {
        super();
        texture = Game.getTexture(textureKey);

        SpriteGraphicsObject gobj = new SpriteGraphicsObject(texture, Game.toPixels(width), Game.toPixels(height));

        gobj.getVertices().bufferToVBO();
        addGraphicsObject(gobj);
    }

    public void setDepth(float depth) {
        getGraphicsObject(0).setDepth(depth);
    }

    public void alignBottom(float y) {
        getPosition().setY(y + Game.toMeters(texture.getPixelHeight()/2));
    }

    public void alignLeft(float x) {
        getPosition().setX(x + Game.toMeters(texture.getPixelWidth()/2));
    }

    public float getWidth() {
        return Game.toMeters(texture.getPixelWidth());
    }

    public float getHeight() {
        return Game.toMeters(texture.getPixelHeight());
    }

    @Override
    public void onStep(float dt) {
        if (getScene().getMinVisibleX() > getPosition().getX() + 10.0f) {
            getScene().removeActor(this);
        }
    }
}
