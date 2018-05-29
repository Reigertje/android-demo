package com.demo.game.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Created by brian on 2-2-18.
 */

public class StringImage extends Texture {

    private int[] glid = new int[1];

    private int textSize;

    private int resourceId;

    public StringImage(int resourceId, int textSize) {
        this.resourceId = resourceId;
        this.textSize = textSize;
        setRectangle(0, 1, 1, 0);
    }

    @Override
    public void bind() {
        if (glid[0] != -1) {
            bindTextureId(glid[0]);
        } else throw new RuntimeException("Unloaded texture bound");
    }

    private Paint setupPaint(Context context) {
        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/Ubuntu-R.ttf");
        Paint paint = new Paint();
        paint.setTypeface(type);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        paint.setARGB(255,255, 255, 255); // Altijd wit, krijgt via graphicsObject kleur
        return paint;
    }

    @Override
    public void load(Context context) {

        final Paint paint = setupPaint(context);

        String line = context.getString(resourceId);

        int pixelHeight = (int)(paint.getFontMetrics().descent - paint.getFontMetrics().ascent + 1);
        int pixelWidth = (int)(paint.measureText(line) + 1);

        setPixelSize(pixelWidth, pixelHeight);

        int yPos = (int) ((pixelHeight / 2) - ((paint.descent() + paint.ascent()) / 2)) ;

        Bitmap bitmap = Bitmap.createBitmap(pixelWidth, pixelHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(line, pixelWidth/2, yPos, paint);

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
