package com.demo.game.resources;

import android.content.Context;

import com.demo.game.graphics.Shader;
import com.demo.game.util.FileUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShaderResources {

    private Map<String, Shader> shaders;

    public ShaderResources(Context context) throws IOException {
        shaders = new HashMap<>();

       addShader("test", new Shader(
                FileUtil.readAssetToString(context.getAssets().open("shaders/object_plain.vert")),
                FileUtil.readAssetToString(context.getAssets().open("shaders/object_plain.frag"))));

        addShader("fbo_default", new Shader(
                FileUtil.readAssetToString(context.getAssets().open("shaders/fbo_plain.vert")),
                FileUtil.readAssetToString(context.getAssets().open("shaders/fbo_plain.frag"))));

        loadAllShaders();
    }

    private void addShader(String key, Shader shader) {
        shaders.put(key, shader);
    }

    public Shader getShader(String key) {
        return shaders.get(key);
    }

    void loadAllShaders() {
        for (Shader shader : shaders.values()) {
            shader.load();
        }
    }

}
