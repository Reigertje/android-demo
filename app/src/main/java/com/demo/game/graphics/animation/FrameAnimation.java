package com.demo.game.graphics.animation;

public class FrameAnimation {

    public static final int DEFAULT_DELAY = 50;

    private static final float ANIMATION_ENDED = 21212121.0f;

    private int numberOfFrames;

    private int[] delays;

    private float previousDelay;

    private float currentDelay;

    int currentFrame;

    boolean repeat;

    float speed;

    public FrameAnimation(int numberOfFrames) {
        this(numberOfFrames, true);
    }

    public FrameAnimation(int numberOfFrames, boolean repeat) {
        this(numberOfFrames, DEFAULT_DELAY, repeat);
    }

    public FrameAnimation(int numberOfFrames, int delay, boolean repeat) {
        this.numberOfFrames = numberOfFrames;
        this.delays = new int[numberOfFrames];
        this.repeat = repeat;
        setUniformDelay(delay);
        setSpeed(1.0f);
        reset();
    }

    public FrameAnimation(int numberOfFrames, int[] delays, boolean repeat) {
        this.numberOfFrames = numberOfFrames;
        this.delays = delays;
        this.repeat = repeat;
        setSpeed(1.0f);
        reset();
    }

    public void reset() {
        setCurrentFrame(0);
        currentDelay = delays[0];
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
        currentDelay = delays[currentFrame];
    }

    public int getNumberOfFrames() {
        return numberOfFrames;
    }

    public boolean hasFinished() {
        return currentDelay == ANIMATION_ENDED;
    }

    public boolean doesRepeat() {
        return repeat;
    }

    public void setUniformDelay(int delay) {
        for (int i = 0; i < numberOfFrames; ++i) delays[i] = delay;
    }

    public void setDelay(int frame, int delay) {
        delays[frame] = delay;
    }

    public void setDelays(int[] delays) {
        for (int i = 0; i < numberOfFrames; ++i) this.delays[i] = delays[i];
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getDurationWithoutSpeed() {
        int sum = 0;
        for (int i = 0; i < numberOfFrames; ++i) {
            sum += delays[i];
        }
        return sum;
    }

    public void smoothTransition(FrameAnimation other) {
        if (other.getNumberOfFrames() == this.getNumberOfFrames()) {
            this.currentFrame = other.getCurrentFrame();
            this.speed = other.getSpeed();
            this.currentDelay = other.currentDelay;
        } else {
            System.err.println("Animation must have equal number of frames for smooth transition!");
        }
    }

    public void step(float dt) {

        currentDelay -= (dt * 1000.0f * speed);

        while (currentDelay <= 0) {
            currentFrame = repeat ? (currentFrame + 1) % numberOfFrames :
                    currentFrame < numberOfFrames - 1 ? currentFrame + 1 :
                            numberOfFrames - 1;

            if (currentFrame == numberOfFrames - 1 && !repeat) {
                currentDelay = ANIMATION_ENDED;
            } else {
                currentDelay += delays[currentFrame];
            }
        }
    }

}
