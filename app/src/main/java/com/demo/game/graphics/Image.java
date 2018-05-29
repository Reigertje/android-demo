package com.demo.game.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.io.IOException;

public class Image extends Texture {

    private String assetName;

    private int[] glid = new int[1];

    public Image(Context context, String assetName) {
        this.assetName = assetName;
        this.glid[0] = -1;

        // Read bitmap size and type only
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getAssets().open(assetName), null, options);

            setPixelSize(options.outWidth, options.outHeight);
            setRectangle(0.0f, 1.0f, 1.0f, 0.0f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void bind() {
        if (glid[0] != -1) {
            bindTextureId(glid[0]);
        } else throw new RuntimeException("Unloaded texture bound");
    }

    @Override
    public void load(Context context) {
        if (this.glid[0] > -1) return;

        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(context.getAssets().open(assetName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        GLES20.glGenTextures(1, glid, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, glid[0]);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();
    }

    @Override
    public void delete() {
        GLES20.glDeleteTextures(1, glid, 0);
    }
}
