package com.demo.game.graphics.renderer;

import android.opengl.GLES20;

import com.demo.game.geom.Mat4x4;
import com.demo.game.Game;
import com.demo.game.graphics.FBO;
import com.demo.game.graphics.GraphicsObject;
import com.demo.game.graphics.GraphicsScene;
import com.demo.game.graphics.Shader;
import com.demo.game.graphics.Texture;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FRAMEBUFFER;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.glBindFramebuffer;
import static android.opengl.GLES20.glBlendColor;
import static android.opengl.GLES20.glBlendEquation;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glUniform1f;
import static android.opengl.GLES20.glViewport;

public class SceneRenderer extends GraphicsScene.Renderer {

    private Mat4x4 projection;

    public static int renderCount = 0;
    public static int textureBindCalls = 0;

    private float wave_offset;

    private FBO fboTest;

    private Shader defaultShader;

    private Shader fboShader;

    private int pixelWidth, pixelHeight;

    public SceneRenderer(int pixelWidth, int pixelHeight) {
        this.pixelWidth = pixelWidth;
        this.pixelHeight = pixelHeight;
        projection = new Mat4x4();
        projection.setOrtho(0, pixelWidth, 0, pixelHeight, 0, -4000);
        this.defaultShader = Game.getShader("test");
        this.fboShader = Game.getShader("fbo_default");
        this.fboTest = new FBO(pixelWidth, pixelHeight);
        fboTest.init();
    }

    protected void bindShader() {
        defaultShader.bind();
        glEnableVertexAttribArray( defaultShader.getAttribHandler(Shader.VERTEX_XY));
        glEnableVertexAttribArray( defaultShader.getAttribHandler(Shader.VERTEX_UV));
    }

    protected void renderObjects(GraphicsScene scene, float lerp, int layer) {
        for (GraphicsObject graphicsObject : scene.getLayer(layer)) {
            graphicsObject.draw(scene, projection, defaultShader, lerp);
        }
    }

    public void render(GraphicsScene scene, float lerp) {

        wave_offset += 0.1;

        Texture.clearLastBound();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        glBindFramebuffer(GL_FRAMEBUFFER, fboTest.getId());
        glViewport(0, 0, fboTest.getWidth(), fboTest.getHeight());

        glClearColor(0.964f, 0.925f, 0.831f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        bindShader();
        renderObjects(scene, lerp, 0);


        glBlendColor(1, 1, 1, 0.1f);
        glBlendFunc(GLES20.GL_CONSTANT_ALPHA, GLES20.GL_SRC_COLOR);
        renderObjects(scene, lerp, 2);

        glBlendColor(1, 1, 1, 0.15f);
        glBlendFunc(GLES20.GL_CONSTANT_ALPHA, GLES20.GL_ONE);
        renderObjects(scene, lerp, 3);

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0, 0, pixelWidth, pixelHeight);


        fboShader.bind();

        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        glUniform1f(fboShader.getUniformHandler("wave_offset"), wave_offset);

        fboTest.draw(fboShader);

        bindShader();


        renderObjects(scene, lerp, 1);

        renderCount = 0;
        textureBindCalls = 0;
    }

}
