package com.demo.android;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.view.MotionEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.demo.game.Game;
import com.demo.game.input.Controller;

public class GameView extends GLSurfaceView {

    private static final int FRAMES_PER_SECOND = 30;

    private Context context;

    private final List<ClonedMotionEvent> motionEventQueue = new ArrayList<>();

    private final List<Controller.Gesture> gestureQueue = new ArrayList<>();

    public GameView(Context context) {
        super(context);
        this.context = context;
        setEGLContextClientVersion(2);
        setRenderer(new Renderer());
    }

    private void queueMotionEvent(ClonedMotionEvent event) {
        synchronized (motionEventQueue) {
            motionEventQueue.add(event);
        }
    }

    private void queueGesture(Controller.Gesture gesture) {
        synchronized (gestureQueue) {
            gestureQueue.add(gesture);
        }
    }

    public void handleTouchEvent(final MotionEvent event) {
        // Kloon motionevent, omdat deze hergebruikt wordt door android
        final ClonedMotionEvent clonedEvent = ClonedMotionEvent.fromMotionEvent(event);
        if (clonedEvent != null) {
            if (clonedEvent.getAction() == MotionEvent.ACTION_DOWN) performClick();
            queueEvent(() -> queueMotionEvent(clonedEvent));
        }
    }


    public boolean onGesture(final Controller.Gesture gesture) {
        queueEvent(() -> queueGesture(gesture));
        return true;
    }

    private class Renderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            try {
                Game.initialize(context);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Game.setScreenDimensions(width, height);
        }

        private void handleMotionEvent(ClonedMotionEvent motionEvent) {
            int action = motionEvent.getAction();
            int index = motionEvent.getIndex();

            if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
                Game.getInstance().handleClick(motionEvent.getX(), motionEvent.getY(), index);
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_CANCEL) {
                Game.getInstance().handleRelease(index);
            } else {
               // MOVE?
            }
        }

        private void handleMotionEvents() {
            synchronized (motionEventQueue) {
                for (ClonedMotionEvent motionEvent : motionEventQueue) {
                    handleMotionEvent(motionEvent);
                }
                ClonedMotionEvent.clear();
                motionEventQueue.clear();
            }
        }

        private void handleGestures() {
            synchronized (gestureQueue) {
                for (Controller.Gesture gesture : gestureQueue) {
                    Game.getInstance().handleGesture(gesture);
                }
                gestureQueue.clear();
            }
        }

        //TODO uitzoeken wat dit precies doet, waarom het nodig is. IS nu gekopieerd van
        // stack overflow :)
        // avoid GC in your threads. declare nonprimitive variables out of onDraw
        float smoothedDeltaRealTime_ms=16.75f; // initial value, Optionally you can save the new computed value (will change with each hardware) in Preferences to optimize the first drawing frames
        float movAverageDeltaTime_ms=smoothedDeltaRealTime_ms; // mov Average start with default value
        long lastRealTimeMeasurement_ms; // temporal storage for last time measurement

        // smooth constant elements to play with
        static final float movAveragePeriod=40; // #frames involved in average calc (suggested values 5-100)
        static final float smoothFactor=0.1f; // adjusting ratio (suggested values 0.01-0.5)

        // sample with opengl. Works with canvas drawing: public void OnDraw(Canvas c)
        public void onDrawFrame(GL10 gl){
            updateGame(); // divide 1000 if your UpdateGame routine is waiting seconds instead mili-seconds.

            // Moving average calc
            long currTimePick_ms= SystemClock.uptimeMillis();
            float realTimeElapsed_ms;
            if (lastRealTimeMeasurement_ms>0){
                realTimeElapsed_ms=(currTimePick_ms - lastRealTimeMeasurement_ms);
            } else {
                realTimeElapsed_ms=smoothedDeltaRealTime_ms; // just the first time
            }
            movAverageDeltaTime_ms=(realTimeElapsed_ms + movAverageDeltaTime_ms*(movAveragePeriod-1))/movAveragePeriod;

            // Calc a better aproximation for smooth stepTime
            smoothedDeltaRealTime_ms=smoothedDeltaRealTime_ms +(movAverageDeltaTime_ms - smoothedDeltaRealTime_ms)* smoothFactor;

            lastRealTimeMeasurement_ms=currTimePick_ms;
        }

        float totalVirtualRealTime_ms=0;
        float speedAdjustments_ms=0; // to introduce a virtual Time for the animation (reduce or increase animation speed)
        float totalAnimationTime_ms=0;
        float fixedStepAnimation_ms=1000.0f/FRAMES_PER_SECOND; // 20ms for a 50FPS descriptive animation
        int currVirtualAnimationFrame=0; // useful if the updateGameFixedStep routine ask for a frame number

        private void updateGame(){
            handleMotionEvents();
            handleGestures();

            totalVirtualRealTime_ms+=smoothedDeltaRealTime_ms + speedAdjustments_ms;

            while (totalVirtualRealTime_ms> totalAnimationTime_ms){
                totalAnimationTime_ms+=fixedStepAnimation_ms;
                currVirtualAnimationFrame++;
                // original updateGame with fixed step
                Game.getInstance().step(1.0f/FRAMES_PER_SECOND);
            }

            float interpolationRatio=(totalAnimationTime_ms-totalVirtualRealTime_ms)/fixedStepAnimation_ms;

            Game.getInstance().render(1.0f - interpolationRatio);
        }
    }

}
