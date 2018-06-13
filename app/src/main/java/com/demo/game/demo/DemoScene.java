package com.demo.game.demo;

import com.demo.game.actor.actors.ObstacleActor;
import com.demo.game.actor.actors.PlayerActor;
import com.demo.game.actor.actors.WaterActor;
import com.demo.game.interaction.Type;
import com.demo.game.scene.Scene;

/**
 * Created by brian on 29-5-18.
 */

public class DemoScene extends Scene {

    private WaterActor[] causticsActors;
    private int lowestWaterActor;
    private int lowerWaterActorY;

    private PlayerActor player;

    private DemoSceneGenerator generator;
    private int lastGeneratedY = 1920;

    public DemoScene() {

        getGraphicsScene().setCameraOffset(540, 128 + 64);
        // Create and add player
        addActor(player = new PlayerActor());

        // Create water actors
        causticsActors = new WaterActor[3];
        for (int i = 0; i < 3; ++i) {
            causticsActors[i] = new WaterActor();
            causticsActors[i].alignLeft(0);
            causticsActors[i].alignBottom(i * 1920);
            addActor(causticsActors[i]);
        }
        lowestWaterActor = 0;
        lowerWaterActorY = 0;


        player.setPosition(540, 100);


        getInteractionScene().addInteraction(PlayerActor.class, ObstacleActor.class, (scene, player, obstacle, type) -> {
                player.handleObstacleCollision(obstacle);
        });

        generator = new DemoSceneGenerator(this);
    }

    private void updateWaterActors() {
        if (getMinVisibleY() > lowerWaterActorY + 1920) {
            causticsActors[lowestWaterActor].alignBottom(lowerWaterActorY + 3 * 1920);
            lowerWaterActorY += 1920;
            lowestWaterActor = (lowestWaterActor + 1)%3;
        }
    }

    private void updateGenerator() {
        if (getMaxVisibleY() > lastGeneratedY) {
            lastGeneratedY += 384;
            generator.generate(lastGeneratedY);
            lastGeneratedY += 384;
        }
    }

    @Override
    public void updateCameraPosition() {
        setCameraPosition(540, player.getPosition().y);
        super.updateCameraPosition();
    }

    @Override
    public void step(float dt) {
        updateWaterActors();
        updateGenerator();
        super.step(dt);
    }
}
