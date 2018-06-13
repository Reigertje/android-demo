package com.demo.game.actor.actors;

import com.demo.game.Game;
import com.demo.game.actor.Actor;
import com.demo.game.graphics.GraphicsObject;
import com.demo.game.graphics.SpriteGraphicsObject;

public class WaterActor extends StaticImageActor {

    public WaterActor() {
        super("demo_water", 1080, 1920);
        SpriteGraphicsObject shade = new SpriteGraphicsObject(Game.getTexture("demo_water_shade"));
        SpriteGraphicsObject caustics = new SpriteGraphicsObject(Game.getTexture("demo_water_caustics"));

        shade.setLerpEnabled(false);
        caustics.setLerpEnabled(false);

        GraphicsObject water = getGraphicsObject(0);

        water.setLerpEnabled(false);
        shade.setLayer(0);

        water.setLayer(0);
        caustics.setLayer(3);

        water.setDepth(-100);

        shade.setDepth(1);

        addGraphicsObject(shade);
        addGraphicsObject(caustics);
    }

    @Override
    public void onStep(float dt) {

    }
}
