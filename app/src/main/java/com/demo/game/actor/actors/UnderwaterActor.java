package com.demo.game.actor.actors;

import com.demo.game.Game;
import com.demo.game.actor.Actor;
import com.demo.game.geom.Vec2;
import com.demo.game.graphics.GraphicsScene;
import com.demo.game.graphics.SpriteGraphicsObject;

/**
 * Created by brian on 9-6-18.
 */

public class UnderwaterActor extends Actor {

    private SpriteGraphicsObject top, bottom;

    private Vec2 topOffset;

    public UnderwaterActor(String spriteFolder, Vec2 topOffset) {
        this.topOffset = topOffset;
        top = new SpriteGraphicsObject(Game.getTexture(spriteFolder + ".top"));
        bottom = new SpriteGraphicsObject(Game.getTexture(spriteFolder + ".bottom"));

        addGraphicsObjectWithOffset(top, topOffset);
        addGraphicsObject(bottom);

        top.setLayer(GraphicsScene.ABOVE_WATER_LAYER);
        bottom.setLayer(GraphicsScene.BELOW_WATER_LAYER);
    }


    private void updateDepth() {
        bottom.setDepth(getPosition().getY() - bottom.getTexture().getPixelHeight()/2);
        top.setDepth(getPosition().getY() - top.getTexture().getPixelHeight()/2 + topOffset.getY());
    }

    @Override
    public void setPosition(Vec2 position) {
        super.setPosition(position);
        updateDepth();
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        updateDepth();
    }

    @Override
    public void onStep(float dt) {

    }
}


