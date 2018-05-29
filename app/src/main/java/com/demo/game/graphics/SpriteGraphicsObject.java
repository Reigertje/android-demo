package com.demo.game.graphics;

import com.demo.game.geom.Mat4x4;
import com.demo.game.graphics.renderer.SceneRenderer;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform4fv;

public class SpriteGraphicsObject extends GraphicsObject {

    private Texture texture;

    private Vertices vertices;

    public SpriteGraphicsObject(Texture texture, int width, int height) {
        super();
        this.texture = texture;
        this.vertices = new Vertices(4, 6, GL_TRIANGLES);

        this.vertices.setCenteredRectangleVertices(width, height, 0);
        this.vertices.setRectangleTextureCoordinates(texture, 0);
        this.vertices.setRectangleIndices(0, 0);
        setBox(new Box(-width/2, -height/2, width/2, height/2));
    }

    public SpriteGraphicsObject(Texture texture) {
        this(texture, texture.getPixelWidth(), texture.getPixelHeight());
    }

    public SpriteGraphicsObject(Vertices vertices, Texture texture, boolean constructBox) {
        this.texture = texture;
        this.vertices = vertices;
        if (constructBox) {
            float[] v = vertices.getVertices();
            float minx = Integer.MAX_VALUE;
            float maxx = Integer.MIN_VALUE;
            float miny = Integer.MAX_VALUE;
            float maxy = Integer.MIN_VALUE;

            for (int i = 0; i < v.length; i+=2) {
                minx = v[i] < minx ? v[i] : minx;
                maxx = v[i] > maxx ? v[i] : maxx;
                miny = v[i + 1] < miny ? v[i + 1] : miny;
                maxy = v[i + 1] > maxy ? v[i + 1] : maxy;
            }
            setBox(new Box(minx, miny, maxx, maxy));
        }
    }

    public Vertices getVertices() {
        return vertices;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    protected void submitTexture(Shader shader) {
        glActiveTexture(GL_TEXTURE0);
        texture.bind();
        glUniform1i(shader.getUniformHandler(Shader.DIFFUSE_SAMPLER), 0);
    }

    @Override
    public void draw(GraphicsScene scene, Mat4x4 projection, Shader shader, float lerp) {
        // TODO moet al in graphicsscene er uit gefilterd worden zodra
        // draw buffers geimplementeerd zijn
        if (!isVisible() || texture == null) {
            return;
        }
        if (!submitMVP(scene, shader, projection, lerp)) {
            return;
        }
        SceneRenderer.renderCount++;

        submitColors(shader);

        submitTexture(shader);
        vertices.draw(shader);

    }
}
