package com.demo.game.actor.actors;

import com.demo.game.Game;
import com.demo.game.actor.Actor;
import com.demo.game.geom.Circle;
import com.demo.game.geom.Rectangle;
import com.demo.game.graphics.GraphicsObject;
import com.demo.game.graphics.SpriteGraphicsObject;

/**
 * Created by brian on 31-5-18.
 */

public class ObstacleActor extends Actor {

    public static final int LOW = 0;
    public static final int HIGH = 1;

    private int type;

    public ObstacleActor(float width, float height, int type) {

        this.type = type;

        // DEBUG GRAPHICS
       /* GraphicsObject gobj = new SpriteGraphicsObject(Game.getTexture("forestfire"), (int)width, (int)height);
        gobj.setMultiplyColor(1, 1, 1, 0.25f);
        addGraphicsObject(gobj);
        gobj.setDepth(-1000);*/

        setInteractionShape(new Rectangle(width/2, height/2));
    }

    public int getType() {
        return type;
    }

    @Override
    public void onStep(float dt) {

    }
}
