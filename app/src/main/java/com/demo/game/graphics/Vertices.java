package com.demo.game.graphics;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.demo.game.util.BufferUtil;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glVertexAttribPointer;

public class Vertices {

    private static final int VERTEX_SIZE = 2;

    private static final int TEXTURE_COORDINATE_SIZE = 2;

    private float[] vertices;

    private float[] textureCoordinates;

    private short indices[];

    private boolean verticesBuffered;

    private boolean textureCoordinatesBuffered;

    private boolean indicesBuffered;

    private FloatBuffer vertexDataBuffer;

    private FloatBuffer verticesBuffer;

    private FloatBuffer textureCoordinateBuffer;

    private ShortBuffer indicesBuffer;

    private int numberOfVertices;

    private int numberOfIndices;

    private int type;

    public boolean debugPrint = false;

    private int[] vbo;
    private int[] ibo;

    public Vertices(int numberOfVertices, int numberOfIndices, int type) {
        this.vbo = new int[1];
        this.ibo = new int[1];
        this.numberOfVertices = numberOfVertices;
        this.numberOfIndices = numberOfIndices;

        this.vertices = new float[numberOfVertices * VERTEX_SIZE];
        this.verticesBuffer = BufferUtil.getFloatBuffer(vertices.length);

        this.textureCoordinates = new float[numberOfVertices * TEXTURE_COORDINATE_SIZE];
        this.textureCoordinateBuffer = BufferUtil.getFloatBuffer(textureCoordinates.length);

        this.vertexDataBuffer = BufferUtil.getFloatBuffer(vertices.length + textureCoordinates.length);

        if (numberOfIndices > 0) {
            this.indices = new short[numberOfIndices];
            this.indicesBuffer = BufferUtil.getShortBuffer(indices.length);
        }
        this.type = type;
    }

    public Vertices(int numberOfVertices, int type) {
        this(numberOfVertices, 0, type);
    }

    public Vertices(int numberOfVertices) {
        this(numberOfVertices, 0, GLES20.GL_TRIANGLE_FAN);
    }

    public void setVertex(int i, int x, int y) {
        vertices[i * VERTEX_SIZE] = x;
        vertices[i * VERTEX_SIZE + 1] = y;
    }

    public void setTextureCoordinate(int i, float u, float v) {
        textureCoordinates[i * TEXTURE_COORDINATE_SIZE] = u;
        textureCoordinates[i * TEXTURE_COORDINATE_SIZE + 1] = v;
    }

    public void setIndex(int i, short index) {
        indices[i] = index;
    }

    public void setIndex(int i, int index) {
        indices[i] = (short)index;
    }

    public void bufferVertices() {
        verticesBuffer.position(0);
        verticesBuffer.put(vertices);
        verticesBuffered = true;
    }

    public void invalidateVerticesBuffer() {
        verticesBuffered = false;
    }

    public void bufferTextureCoordinates() {
        textureCoordinateBuffer.position(0);
        textureCoordinateBuffer.put(textureCoordinates);
        textureCoordinatesBuffered = true;
    }

    public void invalidateTextureCoordinateBuffer() {
        textureCoordinatesBuffered = false;
    }

    public void bufferIndices() {
        if (numberOfIndices > 0) {
            indicesBuffer.position(0);
            indicesBuffer.put(indices);
            indicesBuffered = true;
        }

    }

    public void invalidateIndicesBuffer() {
        indicesBuffered = false;
    }

    public void bufferToVBO() {
        if (vbo[0] == 0) {
            GLES20.glGenBuffers(1, vbo, 0);
            if (numberOfIndices > 0) {
                GLES20.glGenBuffers(1, ibo, 0);
            }
        }
        vertexDataBuffer.position(0);
        vertexDataBuffer.put(vertices);
        vertexDataBuffer.put(textureCoordinates);
        vertexDataBuffer.position(0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexDataBuffer.capacity() * 4, vertexDataBuffer, GLES20.GL_STATIC_DRAW);

        if (numberOfIndices > 0) {

            if (!indicesBuffered) {
                bufferIndices();
            }

            indicesBuffer.position(0);
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ibo[0]);
            GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer.capacity()
                    * 2, indicesBuffer, GLES20.GL_STATIC_DRAW);
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        }

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

    }

    public void draw(Shader shader) {

        if (!verticesBuffered) bufferVertices();
        verticesBuffer.position(0);
        if (!textureCoordinatesBuffered) bufferTextureCoordinates();
        textureCoordinateBuffer.position(0);
        if (!indicesBuffered) bufferIndices();
        indicesBuffer.position(0);

        if (vbo[0] == 0) {

            glVertexAttribPointer(shader.getAttribHandler(Shader.VERTEX_XY), VERTEX_SIZE, GL_FLOAT, false, 0, verticesBuffer);
            glVertexAttribPointer(shader.getAttribHandler(Shader.VERTEX_UV), VERTEX_SIZE, GL_FLOAT, false, 0, textureCoordinateBuffer);



            if (numberOfIndices > 0) {

                if (debugPrint) {
                    System.out.println("Drawing "  + numberOfIndices + " " + indicesBuffer.capacity());
                    System.out.println("vertices "  + numberOfVertices + " " + verticesBuffer.capacity());
                }
                glDrawElements(type, numberOfIndices, GL_UNSIGNED_SHORT, indicesBuffer);
            } else {
                glDrawArrays(type, 0, numberOfVertices);
            }
        } else {
            // VBO DRAW

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo[0]);


            glVertexAttribPointer(shader.getAttribHandler(Shader.VERTEX_XY), VERTEX_SIZE, GL_FLOAT,
                    false, 0, 0);

            glVertexAttribPointer(shader.getAttribHandler(Shader.VERTEX_UV), VERTEX_SIZE, GL_FLOAT,
                    false, 0, vertices.length * 4);

            if (numberOfIndices > 0) {
                GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, ibo[0]);
                GLES20.glDrawElements(type, numberOfIndices, GLES20.GL_UNSIGNED_SHORT, 0);
                GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
            } else {
                glDrawArrays(type, 0, numberOfVertices);
            }

            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        }
    }

    public void setRectangleVertices(int dx, int dy, int width, int height, int offset) {
        int lwidth = width/2;
        int rwidth = width%2 == 0 ? width/2 : width/2 + 1;

        int bheight = height/2;
        int theight = height%2 == 0 ? height/2 : height/2 + 1;

        setVertex(offset + 0, dx - lwidth, dy - bheight);
        setVertex(offset + 1, dx + rwidth, dy - bheight);
        setVertex(offset + 2, dx + rwidth, dy + theight);
        setVertex(offset + 3, dx - lwidth, dy + theight);
    }

    public void setCenteredRectangleVertices(int width, int height, int offset) {
        setRectangleVertices(0, 0, width, height, offset);
    }

    public void setRectangleTextureCoordinates(Texture texture, int offset) {
        setRectangleTextureCoordinates(texture.getX0(), texture.getY0(), texture.getX1(), texture.getY1(), offset);
    }

    public void setRectangleTextureCoordinates(float x0, float y0, float x1, float y1, int offset) {
        setTextureCoordinate(offset + 0, x0, y0);
        setTextureCoordinate(offset + 1, x1, y0);
        setTextureCoordinate(offset + 2, x1, y1);
        setTextureCoordinate(offset + 3, x0, y1);
    }

    public void setRectangleIndices(int index_offset, int vertex_offset) {
        setIndex(index_offset + 0, vertex_offset + 0);
        setIndex(index_offset + 1, vertex_offset + 1);
        setIndex(index_offset + 2, vertex_offset + 2);
        setIndex(index_offset + 3, vertex_offset + 0);
        setIndex(index_offset + 4, vertex_offset + 2);
        setIndex(index_offset + 5, vertex_offset + 3);
    }

    public float[] getVertices() {
        return vertices;
    }

    public float[] getTextureCoordinates() {
        return textureCoordinates;
    }

    public void print() {
        System.out.println("[");
        for (int i = 0; i < numberOfVertices; ++i) {
            System.out.println("(" + vertices[i * 2] + ", " + vertices[i * 2 + 1] + ")");
        }
        System.out.println("]");
    }

}
