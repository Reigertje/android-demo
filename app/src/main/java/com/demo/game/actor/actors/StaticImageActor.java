package com.demo.game.actor.actors;

import com.demo.game.Game;
import com.demo.game.actor.Actor;
import com.demo.game.graphics.SpriteGraphicsObject;
import com.demo.game.graphics.Texture;

public class StaticImageActor extends Actor {

    private Texture texture;

    private float width, height;

    public StaticImageActor(String textureKey) {
        super();
        texture = Game.getTexture(textureKey);

        SpriteGraphicsObject gobj = new SpriteGraphicsObject(texture);

        gobj.getVertices().bufferToVBO();
        addGraphicsObject(gobj);

        width = Game.toMeters(texture.getPixelWidth());
        height = Game.toMeters(texture.getPixelHeight());
    }

    public StaticImageActor(String textureKey, float width, float height) {
        super();
        texture = Game.getTexture(textureKey);

        SpriteGraphicsObject gobj = new SpriteGraphicsObject(texture, Game.toPixels(width), Game.toPixels(height));

        gobj.getVertices().bufferToVBO();
        addGraphicsObject(gobj);

        this.width = width;
        this.height = height;

    }

    public void setDepth(float depth) {
        getGraphicsObject(0).setDepth(depth);
    }

    public void alignBottom(float y) {
        getPosition().setY(y + height/2);
    }

    public void alignLeft(float x) {
        getPosition().setX(x + width / 2);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    @Override
    public void onStep(float dt) {

    }
}
