package com.pulsar.api;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.pulsar.Statics;
import com.pulsar.api.components.Transform;
import com.pulsar.utils.Utils;

import java.util.ArrayList;

import static com.pulsar.Statics.engine;

public class GameObject implements Cloneable
    , Json.Serializable
{

    public static int counter = 0;

    public String name;
    public int ID = counter++;

    public ArrayList<Component> components = new ArrayList<>();

    public ArrayList<GameObject> children = new ArrayList<>();

    public GameObject parent;
    public int parentID;

    public Transform transform = new Transform();

    public transient boolean renameMode = false;

    public GameObject() {
    }

    public GameObject(String name) {
        this.name = name;
        addComponent(transform);
    }

    void start() {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).start();
        }
        for (int i = 0; i < children.size(); i++) {
            children.get(i).start();
        }
    }

    void update() {
        for (int i = 0; i < components.size(); i++) {
            Component component = components.get(i);
            if (Statics.isGameRunning)
                component.update();
            else
                component.debugUpdate();
        }
        for (int i = 0; i < children.size(); i++) {
            GameObject child = children.get(i);
            if (ID != 0) child.transform.position.set(transform.position.x + child.transform.localPosition.x, transform.position.y + child.transform.localPosition.y);
            child.update();
        }
    }

    void fixedUpdate() {
        for (int i = 0; i < components.size(); i++) {
            Component component = components.get(i);
            if (Statics.isGameRunning) component.fixedUpdate();
        }
        for (int i = 0; i < children.size(); i++) {
            children.get(i).fixedUpdate();
        }
    }

    void lateUpdate() {
        for (int i = 0; i < components.size(); i++) {
            Component component = components.get(i);
            if (Statics.isGameRunning) component.lateUpdate();
        }
        for (int i = 0; i < children.size(); i++) {
            children.get(i).lateUpdate();
        }
    }

    public void addGameObject(GameObject child) {
        child.parent = this;
        children.add(child);
        Statics.allGameObjects.add(child);
    }

    public void removeGameObject(GameObject child) {
        if (!children.contains(child)) return;
        child.parent = null;
        children.remove(child);
        Statics.allGameObjects.removeValue(child, true);
    }

    public void addComponent(Component component) {
        component.className = ClassReflection.getSimpleName(component.getClass());
        components.add(component);
        engine.initComponent(component);
        component.gameObject = this;
    }

    public void addComponent(Class<? extends Component> componentClass) {
        try {
            addComponent(ClassReflection.newInstance(componentClass));
        } catch (ReflectionException e) {
            throw new RuntimeException(e);
        }
    }

    public GameObject clone() {
        GameObject object = new GameObject();
        object.name = this.name;
        for (Component component : components) {

            boolean builtin = Component.isComponentBuiltin(component.getClass());

            Component cmp;

            if (builtin) {
                try {
                    //Class<?> c = ClassReflection.forName(ClassReflection.getSimpleName(component.getClass()));
                    cmp = ClassReflection.newInstance(component.getClass());
                } catch (ReflectionException e) {
                    throw new RuntimeException(e);
                }
            } else {
                cmp = Utils.getComponent(ClassReflection.getSimpleName(component.getClass()));
            }

            object.addComponent(cmp);

            for (Field f : component.fields) {
                try {
                    f.set(cmp, f.get(component));
                } catch (ReflectionException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        for (GameObject child : children) {
            object.addGameObject(child.clone());
        }
        object.transform = object.getComponent(Transform.class);
        this.parent.addGameObject(object);
        return object;
    }

    public boolean hasComponent(Class<? extends Component> componentClass) {
        for (int i = 0; i < components.size(); i++) {
            if (components.get(i).getClass() == componentClass)
                return true;
        }
        return false;
    }

    public <T> T getComponent(Class<T> componentClass) {
        for (Component component : components) {
            if (component.getClass() == componentClass)
                return (T) component;
        }
        return null;
    }

    public void removeComponent(Component component) {
        components.remove(component);
    }

    @Override public void write(Json json) {
        json.writeValue("name", name);
        json.writeValue("id", ID);
        json.writeValue("components", components);
        json.writeValue("children", children);
        json.writeValue("parent_id", parentID);
    }

    @Override public void read(Json json, JsonValue jsonData) {
        name = json.readValue("name", String.class, jsonData);
        ID = json.readValue("id", Integer.class, jsonData);
        components = json.readValue("components", ArrayList.class, jsonData);
        children = json.readValue("children", ArrayList.class, jsonData);
        parentID = json.readValue("parent_id", Integer.class, jsonData);
        parent = Utils.getGameObject(parentID);
    }

}
