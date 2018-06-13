package com.demo.game.graphics.animation;


import com.demo.game.geom.Mat4x4;
import com.demo.game.geom.Vec2;
import com.demo.game.graphics.GraphicsObject;
import com.demo.game.graphics.GraphicsScene;
import com.demo.game.graphics.Shader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SkeletonAnimation extends GraphicsObject {

    private final Vec2 lerpedPosition;

    private Map<String, FrameAnimation> frameAnimationMap;

    private List<Bone> bones;

    private String animation;

    private FrameAnimation frameAnimation;

    public SkeletonAnimation(Map<String, FrameAnimation> frameAnimationMap) {
        this.frameAnimationMap = frameAnimationMap;
        this.bones = new ArrayList<>();
        this.animation = frameAnimationMap.keySet().iterator().next();
        this.frameAnimation = frameAnimationMap.get(animation);
        this.lerpedPosition = new Vec2();
    }

    public void addBone(Bone bone) {
        bones.add(bone);
    }

    public void removeBone(Bone bone) {
        bones.remove(bone);
    }

    public int getNumberOfFramesForAnimation(String animation) {
        FrameAnimation frameAnimation = frameAnimationMap.get(animation);
        return frameAnimation != null ? frameAnimation.getNumberOfFrames() : -1;
    }

    public void setAnimation(String animation) {
        if (this.animation.equals(animation)) {
            return;
        }
        this.animation = animation;
        handleAnimationChange();
    }

    private void handleAnimationChange() {
        this.frameAnimation = frameAnimationMap.get(animation);
        if (frameAnimation != null) {
            frameAnimation.reset();
        }
    }

    public void setAnimationSpeed(float speed) {
        if (frameAnimation != null) {
            frameAnimation.setSpeed(speed);
        }
    }

    public int getAnimationDurationWithoutSpeed() {
        if (frameAnimation != null) {
            return frameAnimation.getDurationWithoutSpeed();
        }
        return 0;
    }

    private void stepBones(float dt) {
        for (Bone bone : bones) {
            bone.setAnimation(animation);
            bone.setFrame(frameAnimation.getCurrentFrame());
            bone.step(dt);
        }
    }

    public void step(float dt) {
        super.step(dt);
        // TODO het moet mogelijk zijn om de animatie in de DRAW te doen, om
        // lerp te vermijden
        stepBones(dt);
        if (frameAnimation != null) {
            frameAnimation.step(dt);
        }
    }

    @Override
    public void draw(GraphicsScene scene, Mat4x4 projection, Shader shader, float lerp) {

        lerpedPosition.set(
            lerp(getLerpPosition().getX(), getPosition().getX(), lerp),
            lerp(getLerpPosition().getY(), getPosition().getY(), lerp)
        );

        for (Bone bone : bones) {

            bone.draw(lerpedPosition, scene, projection, shader, lerp);
        }
    }

    public static class Bone {

        private Map<String, List<KeyFrame>> animationMap;

        private String animation;

        int frame;

        public Bone(Map<String, List<KeyFrame>> animationMap) {
            this.animationMap = animationMap;

        }

        public KeyFrame getKeyFrame() {
            List<KeyFrame> keyFrameList = animationMap.get(animation);
            return keyFrameList != null ? keyFrameList.get(frame) : null;
        }

        public String getAnimation() {
            return animation;
        }

        public void setAnimation(String animation) {
            this.animation = animation;
        }

        public int getFrame() {
            return this.frame;
        }

        public void setFrame(int frame) {
            this.frame = frame;
        }

        private GraphicsObject getGraphicsObject() {
            KeyFrame keyFrame = getKeyFrame();
            return keyFrame != null ? keyFrame.getGraphicsObject() : null;
        }

        public void step(float dt) {
           GraphicsObject graphicsObject = getGraphicsObject();
           if (graphicsObject != null) {
               graphicsObject.step(dt);
           }
        }

        public void draw(Vec2 bodyPostion, GraphicsScene scene, Mat4x4 projection, Shader shader, float lerp) {
            // Body position should already by lerped here
            KeyFrame keyFrame = getKeyFrame();
            if (keyFrame != null) {
                GraphicsObject graphicsObject = getGraphicsObject();
                if (graphicsObject != null) {
                    graphicsObject.setPosition(bodyPostion.getX() + keyFrame.getX(), bodyPostion.getY() + keyFrame.getY());
                    // TODO lerp might as well be disabled here
                    graphicsObject.setLerpEnabled(false);
                    graphicsObject.draw(scene, projection, shader, lerp);
                }
            }
        }
    }

    public static class KeyFrame {

        private GraphicsObject graphicsObject;

        private float x;

        private float y;

        public KeyFrame(GraphicsObject graphicsObject, float x, float y) {
            this.x = x;
            this.y = y;
            this.graphicsObject = graphicsObject;
        }

        public GraphicsObject getGraphicsObject() {
            return graphicsObject;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

    }

}
