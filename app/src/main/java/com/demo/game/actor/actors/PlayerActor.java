package com.demo.game.actor.actors;

import com.demo.game.Game;
import com.demo.game.actor.Actor;
import com.demo.game.geom.Circle;
import com.demo.game.graphics.Color;
import com.demo.game.graphics.SpriteGraphicsObject;
import com.demo.game.graphics.animation.SkeletonAnimation;
import com.demo.game.input.Controller;

/**
 * Created by brian on 30-5-18.
 */

public class PlayerActor extends Actor {

    private static final int LANE_SIZE = 360;

    private static final int JUMP_SIZE = 300;

    private static final int VERTICAL_SPEED = 400;
    private static final int HORIZONTAL_SPEED = 900;

    private int destination_x = -1;

    private int jump_y = -1;

    private float speedFactor = 1.0f;

    private SkeletonAnimation animation;

    private float mixFactor;
    private Color afterHitColor = new Color(1.0f, 0.0f, 0.0f, 1.0f);

    private boolean dead = false;

    public PlayerActor() {

        animation = Game.loadSkeletonAnimation("turtle");

        animation.setAnimation("swim");

        addGraphicsObject(animation);

        setInteractionShape(new Circle(48));

        setInteractionShapeOffset(0, -24);

        // DEBUG FOR INTERACTION SHAPE
        /*addGraphicsObject(new SpriteGraphicsObject(Game.getTexture("forestfire"), 96, 96));
        setGraphicsObjectOffset(1, 0, -24);*/
    }

    private void jump() {
        jump_y = (int)getPosition().getY() + JUMP_SIZE;

        animation.setAnimation("jump");

        float anim_duration_ms = animation.getAnimationDurationWithoutSpeed();
        float jump_duration_ms = ((float)JUMP_SIZE / (VERTICAL_SPEED * speedFactor)) * 1000;

        animation.setAnimationSpeed( anim_duration_ms/ jump_duration_ms);

    }

    private void stopJump() {
        animation.setAnimation("swim");
        jump_y = -1;
    }

    private void moveLeft() {
        if (destination_x - LANE_SIZE > 0 || destination_x == -1) {
            if (destination_x == -1) {
                destination_x = (int)getPosition().getX() - LANE_SIZE;
            } else {
                destination_x -= LANE_SIZE;
            }
        }
    }

    private void moveRight() {
        if (destination_x + LANE_SIZE < Game.VIEW_WIDTH - 1) {
            if (destination_x == -1) {
                destination_x = (int)getPosition().getX() + LANE_SIZE;
             } else {
                destination_x += LANE_SIZE;
            }
        }
    }

    @Override
    public void onStep(float dt) {
        if (dead) return;
        if (mixFactor > 0) {
            mixFactor -= 0.4f * dt;
        }
        if (mixFactor < 0) {
            mixFactor = 0;
        }

        afterHitColor.setAlpha(mixFactor);
        animation.setMixColor(afterHitColor);

        if (jump_y != -1 && getPosition().getY() > jump_y) {
            stopJump();
        }

        if (getScene().getController().wasGesturePerformed(Controller.Gesture.LEFT)) {
            moveLeft();
        }
        if (getScene().getController().wasGesturePerformed(Controller.Gesture.RIGHT)) {
            moveRight();
        }
        if (getScene().getController().wasGesturePerformed(Controller.Gesture.UP)) {
            jump();
        }

        if (destination_x != -1) {
            if (destination_x < getPosition().getX()) {
                getPosition().x = Math.max(destination_x, getPosition().x - HORIZONTAL_SPEED * speedFactor * dt);
            } else if (destination_x > getPosition().getX()) {
                getPosition().x = Math.min(destination_x, getPosition().x + HORIZONTAL_SPEED * speedFactor * dt);
            }
        }

        animation.setLayer(jump_y > -1 ? 1 : 0);

        animation.setDepth(jump_y > -1 ? getPosition().getY() - 130 : getPosition().getY() - 230/2);

        getPosition().y += VERTICAL_SPEED * speedFactor * dt;

        speedFactor += 0.015f * dt;
    }


    public void handleObstacleCollision(ObstacleActor obstacle) {
        if (jump_y > -1 && obstacle.getType() == ObstacleActor.LOW) {
            //System.out.println("JUMP");
        } else {
            if (!dead) {
                Game.gameOverHack();
                dead = true;
            }
        }
    }
}
