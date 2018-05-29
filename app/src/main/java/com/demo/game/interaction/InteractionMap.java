package com.demo.game.interaction;

import com.demo.game.actor.Actor;
import com.demo.game.scene.Scene;

import java.util.HashMap;

public class InteractionMap {

    private HashMap<Integer, Function<?, ?>> map;

    public interface Function<A, B> {
        void f(Scene scene, A a, B b, Type type);
    }

    @SuppressWarnings("unchecked")
    private static <A, B> void callTypedFunction(Function<?, ?> function, Class<A> classA, Class<B> classB, Scene scene, Object a, Object b, Type type) {
        Function<A, B> typedFunction = (Function<A, B>)function;

        if (classA.hashCode() > classB.hashCode()) {
            typedFunction.f(scene, (A)a, (B)b, type);
        } else {
            typedFunction.f(scene, (A)b, (B)a, type);
        }
    }

    private class FunctionObject {
        Function<?, ?> function;
        Class<?> classA;
        Class<?> classB;

        FunctionObject(Function<?, ?> function, Class<?> classA, Class<?> classB) {
            this.function = function;
            this.classA = classA;
            this.classB = classB;
        }

        void call(Scene scene, Object a, Object b, Type type) {
            callTypedFunction(function, classA, classB, scene, a, b, type);
        }
    }

    @SuppressWarnings("AndroidLintUseSparseArrays")
    InteractionMap() {
        map = new HashMap<>();
    }

    private int getHashCodeForClasses(Class<?> a, Class<?> b) {
        int hashA = a.hashCode();
        int hashB = b.hashCode();

        return hashA > hashB ?
                31 * hashA + 17 * hashB :
                17 * hashA + 31 * hashB;
    }

    @SuppressWarnings("unchecked")
    <A, B> void add(Class<A> a, Class<B> b, Function<A, B> function) {
        if (a.hashCode() > b.hashCode()) {
            map.put(getHashCodeForClasses(a, b), function);
        } else {
            map.put(getHashCodeForClasses(a, b), (scene, actor0, actor1, type) -> function.f(scene, (A)actor1, (B)actor0, type));
        }
    }

    private <A, B> FunctionObject findFunctionObject(Class<A> a, Class<B> b) {
        Function<?, ?> function = map.get(getHashCodeForClasses(a, b));
        if (function != null) {
            return new FunctionObject(function, a, b);
        } else {
            FunctionObject result = null;
            if (a.getSuperclass() != Actor.class) {
                result = findFunctionObject(a.getSuperclass(), b);
            }
            if (result == null && b.getSuperclass() != Actor.class) {
                result = findFunctionObject(a, b.getSuperclass());
            }
            return result;
        }
    }

    <A, B> void interact(Scene scene, A a, B b, Type type) {
       FunctionObject functionObject = findFunctionObject(a.getClass(), b.getClass());
       if (functionObject != null) {
           functionObject.call(scene, a, b, type);
       }
    }

}
