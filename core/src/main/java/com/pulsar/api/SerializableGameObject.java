package com.pulsar.api;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.pulsar.api.components.*;
import com.pulsar.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import static com.pulsar.Statics.*;

public class SerializableGameObject {

    public String name;
    public int ID;

    public ArrayList<String> components = new ArrayList<>();

    public HashMap<String, HashMap<String, Object>> componentFields = new HashMap<>();

    public ArrayList<SerializableGameObject> children = new ArrayList<>();

    public SerializableGameObject() { }

    public SerializableGameObject(GameObject gameObject) {
        this.name = gameObject.name;
        this.ID = gameObject.ID;
        for (Component component : gameObject.components) {
            String componentName = "user." + component.getClass().getName();

            if (Component.isComponentBuiltin(component.getClass()))
                componentName = "builtin." + ClassReflection.getSimpleName(component.getClass());

            components.add(componentName);

            HashMap<String, Object> map = new HashMap<>();
            for (com.badlogic.gdx.utils.reflect.Field field : component.fields) {
                try {
                    Object obj = field.get(component);
                    if (obj instanceof Component) {
                        Component cmp = (Component) obj;
                        map.put(field.getName(), "cmp." + cmp.gameObject.ID + "." + cmp.className);
                    } else {
                        map.put(field.getName(), obj);
                    }
                } catch (ReflectionException e) {
                    throw new RuntimeException(e);
                }
            }
            componentFields.put(componentName, map);
        }
        for (GameObject go : gameObject.children) {
            children.add(new SerializableGameObject(go));
        }
    }

    public GameObject createGameObject(GameObject parent) {
        GameObject gameObject = new GameObject(ID);
        gameObject.name = name;
        gameObject.parent = parent;
        for (SerializableGameObject child : children) {
            GameObject o = child.createGameObject(gameObject);
            allGameObjects.get(currentProject.currentScene).add(o);
            gameObject.children.add(o);
        }
        for (String componentName : components) {
            if (componentName.startsWith("builtin.")) {
                try {
                    Class<? extends Component> cls = (Class<? extends Component>) ClassReflection.forName("com.pulsar.api.components." + componentName.substring(8));

                    Component cmp = ClassReflection.newInstance(cls);

                    cmp.className = ClassReflection.getSimpleName(cls);
                    gameObject.components.add(cmp);
                    engine.initComponent(cmp);
                    cmp.gameObject = gameObject;


                    HashMap<String, Object> map = componentFields.get(componentName);

                    for (String fieldName : map.keySet()) {
                        for (com.badlogic.gdx.utils.reflect.Field f : cmp.fields) {
                            if (f.getName().equals(fieldName)) {
                                f.set(cmp, map.get(fieldName));
                                break;
                            }
                        }
                    }

                } catch (ReflectionException e) {
                    throw new RuntimeException(e);
                }
            } else if (componentName.startsWith("user.")) {
                try {
                    Component cmp = Utils.getComponent(componentName.substring(5));

                    HashMap<String, Object> map = componentFields.get(componentName);

                    for (String fieldName : map.keySet()) {
                        for (Field f : cmp.fields) {
                            if (f.getName().equals(fieldName)) {
                                f.set(cmp, map.get(fieldName));
                                break;
                            }
                        }
                    }

                    gameObject.addComponent(cmp);
                } catch (ReflectionException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (gameObject.hasComponent(Transform.class)) {
            gameObject.transform = gameObject.getComponent(Transform.class);
        }
        return gameObject;
    }

}
