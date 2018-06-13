package com.demo.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.demo.android.MainActivity;
import com.demo.game.demo.DemoScene;
import com.demo.game.graphics.Shader;
import com.demo.game.graphics.Texture;
import com.demo.game.graphics.animation.SkeletonAnimation;
import com.demo.game.input.Controller;
import com.demo.game.resources.AnimationResources;
import com.demo.game.resources.ShaderResources;
import com.demo.game.resources.TextureResources;
import com.demo.game.scene.Scene;

import java.io.IOException;

public class Game {

    private static Game instance = null;

    public static final int VIEW_WIDTH = 1080;

    public static final int VIEW_HEIGHT = 1920;

    private static int SCREEN_WIDTH;

    private static int SCREEN_HEIGHT;

    public static final int PIXELS_PER_METER = 1;

    private TextureResources textureResources;

    private ShaderResources shaderResources;

    private AnimationResources animationResources;

    private Scene currentScene;

    private boolean queueRestart;

    private Activity context;

    private Game(Activity context) throws IOException {
        this.context = context;
        this.textureResources = new TextureResources(context);
        this.animationResources = new AnimationResources(context);
        this.shaderResources = new ShaderResources(context);
    }

    public static void initialize(Activity androidContext) throws IOException {
        instance = new Game(androidContext);
        instance.loadPlayerData();
        instance.currentScene = new DemoScene();
    }

    public static void setScreenDimensions(int width, int height) {
        SCREEN_WIDTH = width;
        SCREEN_HEIGHT = height;
    }

    public static Game getInstance() {
        return instance;
    }

    public static Shader getShader(String key) {
        return instance.shaderResources.getShader(key);
    }

    public static Texture getTexture(String key) {
        return instance.textureResources.getTexture(key);
    }

    public static SkeletonAnimation loadSkeletonAnimation(String key) {
        return instance.animationResources.loadSkeletonAnimation(key);
    }

    public static Context getContext() {
        return instance.context;
    }

    public static float toMeters(float pixels) {
        return pixels/PIXELS_PER_METER;
    }

    public static int toPixels(float meters) {
        return Math.round(meters * PIXELS_PER_METER);
    }

    public void handleClick(float x, float y, int pointerIndex) {
        if (currentScene != null) {
            currentScene.handleClick(
                    x / SCREEN_WIDTH,
                    (SCREEN_HEIGHT - y) / SCREEN_HEIGHT,
                    pointerIndex
            );
        }
    }

    public void handleRelease(int pointerIndex) {
        if (currentScene != null) {
            currentScene.handleRelease(pointerIndex);
        }
    }

    public void handleGesture(Controller.Gesture gesture) {
        if (currentScene != null) {
            currentScene.handleGesture(gesture);
        }
    }

    public void step(float dt) {
        if (queueRestart) {
            currentScene = new DemoScene();
            currentScene.step(1.0f/30);
            queueRestart = false;
        }
        if (currentScene != null) {
            currentScene.step(dt);
        }
    }

    public void render(float lerp) {
        if (currentScene != null) {
            currentScene.render(lerp);
        }
    }

    public static void queueRestart() {
        instance.queueRestart = true;
    }

    public static void gameOverHack() {
        instance.context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(instance.context);
                builder.setMessage("GAME OVER :(")
                        .setCancelable(false)
                        .setPositiveButton("START OVER", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Game.queueRestart();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void loadPlayerData() {

    }

}
