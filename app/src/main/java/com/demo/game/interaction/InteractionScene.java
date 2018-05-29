package com.demo.game.interaction;

import android.util.Pair;

import com.demo.game.actor.Actor;
import com.demo.game.scene.Scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.demo.game.interaction.Type.STOP;

public class InteractionScene {

    private InteractionMap interactionMap;

    private List<InteractionObject> interactionObjects;

    private Map<Pair<Actor, Actor>, Type> previousInteractions;

    private Map<Pair<Actor, Actor>, Type> currentInteractions;

    public InteractionScene() {

        interactionMap = new InteractionMap();

        interactionObjects = new ArrayList<>();

        previousInteractions = new HashMap<>();
        currentInteractions = new HashMap<>();
    }

    public void addInteractionObject(InteractionObject interactionObject) {
        this.interactionObjects.add(interactionObject);
    }

    public void removeInteractionObject(InteractionObject interactionObject) {
        this.interactionObjects.remove(interactionObject);
    }

    private void swapAndClearInteractions() {
        Map<Pair<Actor, Actor>, Type> temp = previousInteractions;
        previousInteractions = currentInteractions;
        currentInteractions = temp;
        currentInteractions.clear();
    }

    private boolean didInteractLastStep(Pair<Actor, Actor> pair) {
        Type type = previousInteractions.get(pair);
        return type != null && type != STOP;
    }

    private boolean didInteractThisStep(Pair<Actor, Actor> pair) {
        Type type = currentInteractions.get(pair);
        return type != null && type != STOP;
    }

    private void findCurrentInteractions() {
        for (int i = 0; i < interactionObjects.size(); ++i) {
            InteractionObject object0 = interactionObjects.get(i);
            for (int j = i + 1; j < interactionObjects.size(); ++j) {
                InteractionObject object1 = interactionObjects.get(j);
                if (object0.interactsWith(object1)) {
                    Pair<Actor, Actor> interactionPair = new Pair<>(object0.getActor(), object1.getActor());
                    currentInteractions.put(interactionPair, didInteractLastStep(interactionPair) ? Type.CONTINUE : Type.START);
                }
            }
        }
    }

    private void findStoppedInteractions() {
        for (Map.Entry<Pair<Actor, Actor>, Type> entry : previousInteractions.entrySet()) {
            if (entry.getValue() != STOP && !didInteractThisStep(entry.getKey())) {
                currentInteractions.put(entry.getKey(), STOP);
            }
        }
    }

    private void handleInteractionEvent(Scene scene, Actor a, Actor b, Type type) {
        interactionMap.interact(scene, a, b, type);
    }

    public <A extends Actor, B extends Actor> void addInteraction(Class<A> a, Class<B> b, InteractionMap.Function<A, B> function) {
        interactionMap.add(a, b, function);
    }

    public void step(float dt) {
        swapAndClearInteractions();
        findCurrentInteractions();
        findStoppedInteractions();
        for (Map.Entry<Pair<Actor, Actor>, Type> pair : currentInteractions.entrySet()) {
            handleInteractionEvent(pair.getKey().first.getScene(), pair.getKey().first, pair.getKey().second, pair.getValue());
        }
    }

}
