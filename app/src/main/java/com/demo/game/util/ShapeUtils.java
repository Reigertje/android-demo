package com.demo.game.util;

import com.demo.game.Game;
import com.demo.game.geom.Rectangle;
import com.demo.game.graphics.Texture;

/**
 * Created by brian on 8-1-18.
 */

public abstract class ShapeUtils {

    public static Rectangle texturePixels(Texture texture, float scale) {
        return new Rectangle(texture.getPixelWidth()/2 * scale, texture.getPixelHeight()/2 * scale);
    }

    public static Rectangle textureMeters(Texture texture, float scale) {
        return new Rectangle(Game.toMeters(texture.getPixelWidth()/2 * scale), Game.toMeters(texture.getPixelHeight()/2 * scale));
    }

}
