package com.game;

import com.pulsar.api.Component;
import java.lang.reflect.InvocationTargetException;
import com.pulsar.api.GameObject;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import static com.game.Statics.gameObjectManager;
import static com.game.Statics.isEngine;

public class Utils {

    public static Component getComponent(String componentName) {
        Component component;
        Class<? extends Component> cls;
        try {
            cls = (Class<? extends Component>) ClassReflection.forName("com.game.scripts." + componentName);
            component = ClassReflection.newInstance(cls);
        } catch (ReflectionException e) {
            throw new RuntimeException(e);
        }
        return component;
    }

    public static Class<?> getClass(String componentName) {
        return getComponent(componentName).getClass();
    }

    public static void compile() {
        System.out.println("COMPILED");
    }

    private static GameObject search(GameObject parent, int ID) {
        for (GameObject gameObject : parent.children) {
            if (gameObject.ID == ID) {
                return gameObject;
            } else {
                return search(gameObject, ID);
            }
        }
        return null;
    }

    public static GameObject getGameObject(int ID) {
        return search(gameObjectManager.main, ID);
    }

    public static FileHandle getFile(String path) {
        if (isEngine)
            return Gdx.files.absolute(path);
        else
            return Gdx.files.internal(path.substring(path.indexOf("Assets") + 7));
    }

}
