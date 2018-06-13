package com.demo.game.demo;

import com.demo.game.actor.actors.ObstacleActor;
import com.demo.game.actor.actors.UnderwaterActor;
import com.demo.game.geom.Vec2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by brian on 8-6-18.
 */

public class DemoSceneGenerator {

    private static final char SAFE = 'S';
    private static final char WALL = 'W';
    private static final char OBSTACLE = 'O';

    private List<Character> queue = new ArrayList<>();

    private Random random;

    private DemoScene scene;

    public DemoSceneGenerator(DemoScene scene) {
        this.scene = scene;
        this.random = new Random();
    }

    private void addPassageToQueue() {
        queue.add(random.nextInt() % 2 == 0 ? SAFE : OBSTACLE);
    }

    private void addCell() {
        int rand = random.nextInt() % 4;
        queue.add(rand == 0 ? SAFE : rand == 1 ? OBSTACLE : WALL);
    }

    private void placeReeds(float x, float y) {
        UnderwaterActor reed0 = new UnderwaterActor("reed", new Vec2(0, 74 + 125));
        UnderwaterActor reed1 = new UnderwaterActor("reed", new Vec2(0, 74 + 125));
        UnderwaterActor reed2 = new UnderwaterActor("reed", new Vec2(0, 74 + 125));

        if (Math.random() < 0.5) {
            reed0.setPosition(x + 360/2, y + 80);
            reed1.setPosition(x + 360/4, y + 140);
            reed2.setPosition(x + 360/2 + 360/4, y + 140);
        } else {
            reed0.setPosition(x + 360/2, y + 140);
            reed1.setPosition(x + 360/4, y + 80);
            reed2.setPosition(x + 360/2 + 360/4, y + 80);
        }

        scene.addActor(reed0);
        scene.addActor(reed1);
        scene.addActor(reed2);
    }

    private void placeFence(float x, float y) {
        UnderwaterActor leftPole = new UnderwaterActor("pole", new Vec2(0, 71 + 81));
        leftPole.setPosition(x, y + 88);

        UnderwaterActor rightPole = new UnderwaterActor("pole", new Vec2(0, 71 + 81));
        rightPole.setPosition(x + 360, y + 88);

        UnderwaterActor fenceActor = new UnderwaterActor("fence", new Vec2(0, 70 + 20));
        fenceActor.setPosition(x + 360/2, y + 179 / 2);

        scene.addActor(rightPole);
        scene.addActor(leftPole);
        scene.addActor(fenceActor);
    }

    private void generateObject(char type, float x, float y) {
        if (type == WALL) {

            placeReeds(x, y);

            ObstacleActor obstacle = new ObstacleActor(300, 384/4, ObstacleActor.HIGH);
            obstacle.setPosition(x + 360/2, y + 384/6);
            scene.addActor(obstacle);
        }

        if (type == OBSTACLE) {
            placeFence(x,y);

            ObstacleActor obstacle = new ObstacleActor(360, 384/8, ObstacleActor.LOW);
            obstacle.setPosition(x + 360/2, y + 384/8);
            scene.addActor(obstacle);
        }
    }

    public void generate(float y) {

        queue.clear();
        addPassageToQueue();
        addCell();
        addCell();

        Collections.shuffle(queue);

        for (int i = 0; i < queue.size(); ++i) {
            generateObject(queue.get(i), i * 360, y);
        }

    }

}
