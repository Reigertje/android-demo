package com.demo.game.graphics;

import android.opengl.GLES20;

import com.demo.game.util.BufferUtil;

import java.nio.FloatBuffer;

/**
 * Created by brian on 6-9-17.
 */

public class FBO {

    private static final int FBO_ID = 0;
    private static final int CBO_ID = 1;

    private static final float [] vertices = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            1.0f, 1.0f,
            -1.0f, 1.0f
    };

    private static final float [] flippedTcoords = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f
    };

    private FloatBuffer verticesBuffer;

    private FloatBuffer flippedTCoordsBuffer;

    private int width, height;

    int buf[];

    public FBO(int width, int height) {
        this.width = width;
        this.height = height;

        verticesBuffer = BufferUtil.getFloatBuffer(vertices.length);
        flippedTCoordsBuffer = BufferUtil.getFloatBuffer(flippedTcoords.length);

        verticesBuffer.put(vertices);
        flippedTCoordsBuffer.put(flippedTcoords);

        buf = new int[2];
    }

    private boolean checkStatus() {
        int status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);
        switch(status) {
            case GLES20.GL_FRAMEBUFFER_COMPLETE:
                return true;
            case GLES20.GL_FRAMEBUFFER_UNSUPPORTED:
                System.err.println("Framebuffer not supported");
                return false;
            default:
        	/* programming error; will fail on all hardware */
                System.err.println("Framebuffer failed due to software");
                return false;
        }
    }

    public void init() {
        GLES20.glGenBuffers(1, buf, FBO_ID);
        GLES20.glGenTextures(1, buf, CBO_ID);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, buf[FBO_ID]);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, buf[CBO_ID]);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, buf[CBO_ID], 0);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        checkStatus();
    }

    public void draw(Shader shader) {
        verticesBuffer.position(0);
        flippedTCoordsBuffer.position(0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, buf[CBO_ID]);
        GLES20.glUniform1i(shader.getUniformHandler(Shader.DIFFUSE_SAMPLER), 0);

        GLES20.glVertexAttribPointer(shader.getAttribHandler(Shader.VERTEX_XY), 2, GLES20.GL_FLOAT, false, 0, verticesBuffer);
        GLES20.glVertexAttribPointer(shader.getAttribHandler(Shader.VERTEX_UV), 2, GLES20.GL_FLOAT, false, 0, flippedTCoordsBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
    }

    public void bindTexture() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, buf[CBO_ID]);
    }

    public int getId() {
        return buf[FBO_ID];
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
