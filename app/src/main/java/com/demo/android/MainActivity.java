package com.demo.android;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.demo.game.input.Controller;

public class MainActivity extends Activity implements GestureDetector.OnGestureListener {

    private GameView gameView;

    private GestureDetectorCompat detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(gameView = new GameView(this));
        gameView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        detector = new GestureDetectorCompat(this, this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gameView.handleTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        float dx = e1.getX() - e2.getX();
        float dy = e1.getY() - e2.getY();

        boolean horizontal = Math.abs(dx) > Math.abs(dy);

        if (!horizontal && dy > 0) {
            gameView.onGesture(Controller.Gesture.UP);
        } else if (!horizontal) {
            gameView.onGesture(Controller.Gesture.DOWN);
        } else if (dx < 0) {
            gameView.onGesture(Controller.Gesture.RIGHT);
        } else {
            gameView.onGesture(Controller.Gesture.LEFT);
        }

        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

}
