package com.demo.game.resources;

import android.content.Context;

import com.demo.game.graphics.SubImage;
import com.demo.game.graphics.Texture;
import com.demo.game.util.FileUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import com.demo.game.graphics.Image;

public class TextureResources {

    private Map<String, Texture> textures;

    private Context context;

    public TextureResources(Context context) throws IOException {
        this.textures = new HashMap<>();
        this.context = context;
        readImagesFromAssetFolder(context, "img", context.getAssets().list("img"));

        loadAllResources();
    }

    public Texture getTexture(String key) {
        Texture t = textures.get(key);
        if (t == null) throw new NoSuchElementException("No texture with key " + key);
        return t;
    }

    private void addTexture(String key, Texture texture) {
        textures.put(key, texture);
    }

    private String generateImageKey(String path) {
        return path.replace("img/", "").replace(".png", "").replaceAll("/", ".");
    }

    private String generateSubImageKey(String path, String name) {
        return path.replace("img/", "").replace(".sht", "").replaceAll("/", ".").concat("." + name);
    }

    private boolean isSupportedImageFile(String filePath) {
        return filePath.length() > 4 && filePath.substring(filePath.length() - 4).equals(".png");
    }

    private static boolean hasSheetFile(String file, String[] files) {
        String sheetName = file.replace(".png", ".sht");
        for (int i = 0; i < files.length; ++i) {
            if (files[i].equals(sheetName)) return true;
        }
        return false;
    }

    private void readSheetFile(Context context, String path, Image image) {
        try {
            String asString = FileUtil.readAssetToString(context.getAssets().open(path));
            JSONObject jsonObject = new JSONObject(asString);

            JSONArray jsonArray = jsonObject.getJSONArray("subimages");
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject subImage = jsonArray.getJSONObject(i);
                addTexture(
                        generateSubImageKey(path, subImage.getString("name")),
                        new SubImage(image, subImage.getInt("x"), subImage.getInt("y"), subImage.getInt("width"), subImage.getInt("height"))
                );
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not open file " + path);
        }
    }

    private void readImagesFromAssetFolder(Context context, String path, String[] files) {
        try {
            for (String file : files) {
                String filePath = path + "/" + file;
                String [] subfiles = context.getAssets().list(filePath);
                if (!file.contains(".") || subfiles.length > 0) {
                    // directory
                    readImagesFromAssetFolder(context, filePath, subfiles);
                } else {
                    // file
                    if (isSupportedImageFile(filePath)) {
                        Image image = new Image(context, filePath);
                        addTexture(generateImageKey(filePath), image);
                        if (hasSheetFile(file, files)) readSheetFile(context, filePath.replace(".png", ".sht"), image);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read resource directory " + path);
        }
    }

    void loadAllResources() {
        // TODO slimmer als subset laden etc.
        for (Texture texture : textures.values()) {
            texture.load(context);
        }
    }

}
