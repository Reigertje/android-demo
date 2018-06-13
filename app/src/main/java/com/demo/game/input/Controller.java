package com.demo.game.input;

public class Controller {

    public enum Gesture {
        UP,
        LEFT,
        DOWN,
        RIGHT
    }

    public enum Action {
        TOUCH
    }

    private ActionState[] actionStates;

    private boolean[] gesturePerformed;

    public Controller() {
        actionStates = new ActionState[Action.values().length];
        for (int i = 0; i < actionStates.length; ++i) {
            actionStates[i] = new ActionState();
        }
        gesturePerformed = new boolean[Gesture.values().length];
    }

    private ActionState getActionState(Action action) {
        return actionStates[action.ordinal()];
    }

    public void startAction(Action action) {
        getActionState(action).start();
    }

    public void stopAction(Action action) {
        getActionState(action).stop();
    }

    public boolean wasActionStarted(Action action) {
        return getActionState(action).wasStarted;
    }

    public boolean wasActionStopped(Action action) {
        return getActionState(action).wasStopped;
    }

    public boolean isActionOngoing(Action action) {
        return getActionState(action).ongoing;
    }

    public void performGesture(Gesture gesture) {
        gesturePerformed[gesture.ordinal()] = true;
    }

    public boolean wasGesturePerformed(Gesture gesture) {
        return gesturePerformed[gesture.ordinal()];
    }

    public void step() {
        for (ActionState actionState : actionStates) {
            actionState.step();
        }
        for (int i = 0; i < gesturePerformed.length; ++i) {
            gesturePerformed[i] = false;
        }
    }


    private class ActionState {

        private boolean wasStarted;
        private boolean ongoing;
        private boolean wasStopped;

        void start() {
            wasStarted = true;
            ongoing = true;
        }

        void stop() {
            wasStopped = true;
            ongoing = false;
        }

        public void step() {
            wasStarted = false;
            wasStopped = false;
        }

    }

}