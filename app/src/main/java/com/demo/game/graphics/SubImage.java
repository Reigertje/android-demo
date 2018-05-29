package com.demo.game.graphics;

import android.content.Context;

public class SubImage extends Texture {

    private Image image;

    public SubImage(Image image, int x, int y, int width, int height) {
        this.image = image;
        setPixelSize(width, height);
        setRectangle(x / (float) image.getPixelWidth(),
                (y + height) / (float) image.getPixelHeight(),
                (x + width) /(float) image.getPixelWidth(),
                y / (float) image.getPixelHeight());
    }

    @Override
    public void bind() {
        image.bind();
    }

    @Override
    public void load(Context context) {
        image.load(context);
    }

    @Override
    public void delete() {
        //TODO over nadenken
    }
}
