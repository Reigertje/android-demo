package com.demo.game.scene;

import com.demo.game.geom.Vec2;
import com.demo.game.graphics.GraphicsObject;
import com.demo.game.graphics.GraphicsScene;
import com.demo.game.input.Controller;
import com.demo.game.interaction.InteractionObject;
import java.util.ArrayList;
import java.util.List;
import com.demo.game.Game;
import com.demo.game.actor.Actor;
import com.demo.game.graphics.renderer.SceneRenderer;
import com.demo.game.interaction.InteractionScene;

public abstract class Scene {

    private List<Actor> actors;

    private List<Actor> actorsToAdd;

    private List<Actor> actorsToRemove;

    private GraphicsScene graphicsScene;

    private InteractionScene interactionScene;

    private SceneRenderer renderer;

    private Controller controller;

    private Vec2 cameraPosition;


    public Scene() {
        actors = new ArrayList<>();
        actorsToAdd = new ArrayList<>();
        actorsToRemove = new ArrayList<>();
        graphicsScene = new GraphicsScene();
        interactionScene = new InteractionScene();
        controller = new Controller();
        cameraPosition = new Vec2();
        renderer = new SceneRenderer(Game.VIEW_WIDTH, Game.VIEW_HEIGHT);

    }

    public void addActor(Actor actor) {
        actorsToAdd.add(actor);
    }

    public void removeActor(Actor actor) {
        actorsToRemove.add(actor);
    }

    public void addGraphicsObject(GraphicsObject graphicsObject) {
        graphicsScene.addObject(graphicsObject);
    }

    public void removeGraphicsObject(GraphicsObject graphicsObject) {
        graphicsScene.removeObject(graphicsObject);
    }

    public void addInteractionObject(InteractionObject interactionObject) {
        interactionScene.addInteractionObject(interactionObject);
    }

    public void removeInteractionObject(InteractionObject interactionObject) {
        interactionScene.removeInteractionObject(interactionObject);
    }


    public GraphicsScene getGraphicsScene() {
        return graphicsScene;
    }

    public InteractionScene getInteractionScene() {
        return interactionScene;
    }

    public Controller getController() {
        return controller;
    }

    public void setCameraPosition(Vec2 position) {
        this.cameraPosition.set(position);
    }

    public void setCameraPosition(float x, float y) {
        this.cameraPosition.set(x, y);
    }

    public Vec2 getCameraPosition() {
        return cameraPosition;
    }

    public float getMinVisibleX() {
        return getCameraPosition().x - Game.toMeters(graphicsScene.getCameraOffset().x);
    }

    public float getMaxVisibleX() {
        return getMinVisibleX() + Game.toMeters(Game.VIEW_WIDTH);
    }

    public float getMinVisibleY() {
        return getCameraPosition().y - Game.toMeters(graphicsScene.getCameraOffset().y);
    }

    public float getMaxVisibleY() {
        return getMinVisibleY() + Game.toMeters(Game.VIEW_HEIGHT);
    }

    public float getMinVisibleX(float parallax) {
        return (parallax) * getMinVisibleX();
    }

    public float getMaxVisibleX(float parallax) {
        return getMinVisibleX(parallax) + Game.toMeters(Game.VIEW_WIDTH);
    }

    public float getMinVisibleY(float parallax) {
        return parallax * getCameraPosition().y - Game.toMeters(graphicsScene.getCameraOffset().y);
    }

    public float getMaxVisibleY(float parallax) {
        return parallax * getMinVisibleY() + Game.toMeters(Game.VIEW_HEIGHT);
    }

    private void synchronizeActors() {
        for (Actor actor : actorsToAdd) {
            actors.add(actor);
            actor.addToScene(this);
        }
        for (Actor actor : actorsToRemove) {
            actor.removeFromScene();
            actors.remove(actor);
        }
        actorsToAdd.clear();
        actorsToRemove.clear();
    }

    public void updateCameraPosition() {
        getGraphicsScene().lerpCameraFromCurrentPosition();
        getGraphicsScene().setCameraPosition(
                Game.toPixels(cameraPosition.getX()),
                Game.toPixels(cameraPosition.getY())
        );
    }

    public void step(float dt) {


        synchronizeActors();

        for (Actor actor : actors) {
            actor.step(dt);
        }


        updateCameraPosition();

        interactionScene.step(dt);

        controller.step();

        graphicsScene.step();
    }

    public void render(float lerp) {
        renderer.render(graphicsScene, lerp);
    }

    public void handleClick(float x, float y, int pointerIndex) {
        getController().startAction(Controller.Action.TOUCH);
    }

    public void handleRelease(int pointerIndex) {
        getController().startAction(Controller.Action.TOUCH);
    }

    public void handleGesture(Controller.Gesture gesture) {
        controller.performGesture(gesture);
    }

    public void startControllerAction(Controller.Action action) {
        getController().startAction(action);
    }

    public void stopControllerAction(Controller.Action action) {
        getController().stopAction(action);
    }

}
