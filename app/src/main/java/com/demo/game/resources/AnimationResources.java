package com.demo.game.resources;

import android.content.Context;

import com.demo.game.Game;
import com.demo.game.graphics.SpriteGraphicsObject;
import com.demo.game.graphics.Texture;
import com.demo.game.graphics.animation.FrameAnimation;
import com.demo.game.graphics.animation.SkeletonAnimation;
import com.demo.game.util.FileUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AnimationResources {

    private Map<String, JSONObject> skeletonData;

    private Map<String, JSONObject> boneData;

    public AnimationResources(Context context) throws IOException  {
        this.skeletonData = new HashMap<>();
        this.boneData = new HashMap<>();
        readFromAssetFolder(context, "animation", context.getAssets().list("animation"));
    }

    public SkeletonAnimation loadSkeletonAnimation(String key) {
        JSONObject data = skeletonData.get(key);
        return data != null ? skeletonFromJSON(key, data) : null;
    }

    public SkeletonAnimation.Bone loadSkeletonAnimationBone(String key) {
        JSONObject data = boneData.get(key);
        return data != null ? boneFromJSON(key, data) : null;
    }

    private int[] delaysFromJSON(int frameCount, JSONObject json) throws JSONException {
        int[] result = new int[frameCount];

        if (!json.isNull("delay")) {
            int delay = json.getInt("delay");
            for (int i = 0; i < frameCount; ++i) result[i] = delay;
        } else if (!json.isNull("delays")) {
            JSONArray arr = json.getJSONArray("delays");
            for (int i = 0; i < arr.length(); ++i) result[i] = arr.getInt(i);
        } else {
            for (int i = 0; i < frameCount; ++i) result[i] = FrameAnimation.DEFAULT_DELAY;
        }

        return result;
    }

    private SkeletonAnimation skeletonFromJSON(String key, JSONObject json) {
        try {
            // default bone keyframes
            Map<String, List<SkeletonAnimation.KeyFrame>> keyFrameMap = null;

            JSONArray jsonAnimations = json.getJSONArray("animations");
            Map<String, FrameAnimation> animationMap = new HashMap<>();

            for (int i = 0; i < jsonAnimations.length(); ++i) {
                JSONObject jsonAnimation = jsonAnimations.getJSONObject(i);

                String name = jsonAnimation.getString("name");

                int frameCount;
                if (jsonAnimation.isNull("frames")) {
                    frameCount = jsonAnimation.getInt("frameCount");
                } else {
                    JSONArray jsonFrames = jsonAnimation.getJSONArray("frames");
                    frameCount = jsonFrames.length();
                    List<SkeletonAnimation.KeyFrame> keyFrames = keyFramesFromJSON(jsonFrames);
                    if (keyFrameMap == null) keyFrameMap = new HashMap<>();
                    keyFrameMap.put(name, keyFrames);
                }

                animationMap.put(name, new FrameAnimation(
                        frameCount,
                        delaysFromJSON(frameCount, jsonAnimation),
                        jsonAnimation.getBoolean("repeat")
                ));
            }

            SkeletonAnimation result = new SkeletonAnimation(animationMap);
            if (keyFrameMap != null) {
                result.addBone(new SkeletonAnimation.Bone(keyFrameMap));
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading skeleton data " + key);
        }
    }

    private SkeletonAnimation.KeyFrame keyFrameFromJSON(JSONObject json) throws JSONException {
        Texture texture = Game.getTexture(json.getString("image"));

        float scale = 1.0f;

        if (!json.isNull("scale")) {
            scale = (float)json.getDouble("scale");
        }

        return new SkeletonAnimation.KeyFrame(
                new SpriteGraphicsObject(texture, (int)(texture.getPixelWidth() * scale), (int)(texture.getPixelHeight() * scale)),
                json.getInt("x"),
                json.getInt("y")
        );
    }

    private List<SkeletonAnimation.KeyFrame> keyFramesFromJSON(JSONArray jsonArray) throws JSONException {
        List<SkeletonAnimation.KeyFrame> keyFrames = new ArrayList<>();
        for (int j = 0; j < jsonArray.length(); ++j) {
            if (jsonArray.isNull(j)) keyFrames.add(null);
            else {
                keyFrames.add(keyFrameFromJSON(jsonArray.getJSONObject(j)));
            }
        }
        return keyFrames;
    }

    private SkeletonAnimation.Bone boneFromJSON(String key, JSONObject json) {
        try {
            JSONArray jsonAnimations = json.getJSONArray("animations");
            Map<String, List<SkeletonAnimation.KeyFrame>> keyFrameMap = new HashMap<>();

            for (int i = 0; i < jsonAnimations.length(); ++i) {
                JSONObject jsonAnimation = jsonAnimations.getJSONObject(i);
                List<SkeletonAnimation.KeyFrame> keyFrames = keyFramesFromJSON(jsonAnimation.getJSONArray("frames"));
                keyFrameMap.put(jsonAnimation.getString("name"), keyFrames);
            }
            return new SkeletonAnimation.Bone(keyFrameMap);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading bone data " + key);
        }
    }

    private boolean isSkeletonFile(String filePath) {
        return filePath.length() > 4 && filePath.substring(filePath.length() - 4).equals(".skl");
    }

    private boolean isBoneFile(String filePath) {
        return filePath.length() > 4 && filePath.substring(filePath.length() - 4).equals(".bon");
    }

    private String generateSkeletonKey(String path) {
        return path.replace("animation/", "").replace(".skl", "").replaceAll("/", ".");
    }

    private String generateBoneKey(String path) {
        return path.replace("animation/", "").replace(".bon", "").replaceAll("/", ".");
    }

    private void storeSkeletonData(Context context, String path) {
        try {
            JSONObject jsonObject = new JSONObject(FileUtil.readAssetToString(context.getAssets().open(path)));
            skeletonData.put(generateSkeletonKey(path), jsonObject);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not open file " + path);
        }
    }

    private void storeBoneData(Context context, String path) {
        try {
            JSONObject jsonObject = new JSONObject(FileUtil.readAssetToString(context.getAssets().open(path)));
            boneData.put(generateBoneKey(path), jsonObject);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not open file " + path);
        }
    }

    public void readFromAssetFolder(Context context, String path, String[] files) {
        try {
            for (String file : files) {
                String filePath = path + "/" + file;
                String [] subfiles = context.getAssets().list(filePath);
                if (!file.contains(".") || subfiles.length > 0) {
                    // directory
                    readFromAssetFolder(context, filePath, subfiles);
                } else if (isSkeletonFile(filePath)) {
                    // skeleton
                    storeSkeletonData(context, filePath);
                } else if (isBoneFile(filePath)) {
                    // bone
                    storeBoneData(context, filePath);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read resource directory " + path);
        }
    }

    public void loadAllResources() {
        // Niks te doen
    }
}
