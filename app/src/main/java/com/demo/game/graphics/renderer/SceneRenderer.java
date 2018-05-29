package com.demo.game.graphics.renderer;

import com.demo.game.geom.Mat4x4;
import com.demo.game.Game;
import com.demo.game.graphics.GraphicsObject;
import com.demo.game.graphics.GraphicsScene;
import com.demo.game.graphics.Shader;
import com.demo.game.graphics.Texture;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;

public class SceneRenderer extends GraphicsScene.Renderer {

    private Mat4x4 projection;

    public static int renderCount = 0;
    public static int textureBindCalls = 0;

    private Shader shader;

    public SceneRenderer(int pixelWidth, int pixelHeight) {
        projection = new Mat4x4();
        projection.setOrtho(0, pixelWidth, 0, pixelHeight, 0, -4000);
        this.shader = Game.getShader("test");
    }

    protected Shader getShader() {
        return shader;
    }

    protected void bindShader() {
        shader.bind();
        glEnableVertexAttribArray( shader.getAttribHandler(Shader.VERTEX_XY));
        glEnableVertexAttribArray( shader.getAttribHandler(Shader.VERTEX_UV));
    }

    protected void renderObjects(GraphicsScene scene, float lerp) {
        for (GraphicsObject graphicsObject : scene.getLayer(0)) {
            graphicsObject.draw(scene, projection, shader, lerp);
        }
    }

    public void render(GraphicsScene scene, float lerp) {

        Texture.clearLastBound();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        bindShader();
        renderObjects(scene, lerp);

        renderCount = 0;
        textureBindCalls = 0;
    }

}
