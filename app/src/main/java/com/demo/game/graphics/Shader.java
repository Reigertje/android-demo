package com.demo.game.graphics;

import android.opengl.GLES20;

import java.util.HashMap;
import java.util.Map;

public class Shader {

    public static final String MVP = "mvp";
    public static final String VERTEX_XY = "vertex_xy";
    public static final String VERTEX_UV = "vertex_uv";
    public static final String DIFFUSE_SAMPLER = "diffuse_sampler";
    public static final String MIX_COLOR = "mix_color";
    public static final String MULTIPLY_COLOR = "multiply_color";

    private String vertexShader;
    private String fragmentShader;

    private int glid = -1;

    private Map<String, Integer> handlerMap;

    public Shader(String vertexShader, String fragmentShader) {
        this.handlerMap = new HashMap<>();
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    private void compile() {
        int vertShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        int fragShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        int result = GLES20.GL_FALSE;

        int compiled[] = new int[1];

        // Compile vertex shader
        GLES20.glShaderSource(vertShader, vertexShader);
        GLES20.glCompileShader(vertShader);

        // Check vertex shader
        GLES20.glGetShaderiv(vertShader, GLES20.GL_COMPILE_STATUS, compiled, 0);

        if (compiled[0] == 0) {
            throw new RuntimeException("Vertex shader did not compile : " + GLES20.glGetShaderInfoLog(vertShader));
        }

        // Compile fragment shader
        GLES20.glShaderSource(fragShader, fragmentShader);
        GLES20.glCompileShader(fragShader);

        // Check fragment shader
        GLES20.glGetShaderiv(fragShader, GLES20.GL_COMPILE_STATUS, compiled, 0);

        if (compiled[0] == 0) {
            throw new RuntimeException("Fragment shader did not compile : " + GLES20.glGetShaderInfoLog(fragShader));
        }

        // Link
        glid = GLES20.glCreateProgram();
        GLES20.glAttachShader(glid, vertShader);
        GLES20.glAttachShader(glid, fragShader);
        GLES20.glLinkProgram(glid);

        GLES20.glGetProgramiv(glid, GLES20.GL_LINK_STATUS, compiled, 0);

        if (compiled[0] == 0) {
            throw new RuntimeException("Error linking shader program : " + GLES20.glGetProgramInfoLog(glid));
        }

        GLES20.glDeleteShader(vertShader);
        GLES20.glDeleteShader(fragShader);
    }

    public void load() {
        if (glid == -1) compile();
    }

    public void bind() {
        if (glid == -1) {
            throw new RuntimeException("Usage of uncompiled shader");
        }
        GLES20.glUseProgram(glid);
    }

    public int getUniformHandler(String key) {
        Integer result = handlerMap.get(key);
        if (result == null) {
            result = GLES20.glGetUniformLocation(glid, key);
            handlerMap.put(key, result);
        }
        return result;
    }

    public int getAttribHandler(String key) {
        Integer result = handlerMap.get(key);
        if (result == null) {
            result = GLES20.glGetAttribLocation(glid, key);
            handlerMap.put(key, result);
        }
        return result;
    }


}
