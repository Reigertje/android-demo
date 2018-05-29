package com.demo.game.demo;

import com.demo.game.actor.actors.StaticImageActor;
import com.demo.game.scene.Scene;

/**
 * Created by brian on 29-5-18.
 */

public class DemoScene extends Scene {

    public DemoScene() {

        StaticImageActor testActor = new StaticImageActor("forestfire");

        addActor(testActor);

    }

}
