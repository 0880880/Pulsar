package com.pulsar.api;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.pulsar.api.components.Transform;

import java.util.ArrayList;

import static com.game.Statics.engine;

public class GameObjectTest implements Cloneable, Json.Serializable {

    public String name;

    public ArrayList<Component> components = new ArrayList<>();

    public ArrayList<GameObjectTest> children = new ArrayList<>();

    public GameObjectTest parent;

    public Transform transform = new Transform();

    public boolean renameMode = false; // Don't add this to user API

    public GameObjectTest() {
        addComponent(transform);
    }

    public GameObjectTest(String name) {
        this.name = name;
        addComponent(transform);
    }

    public void start() {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).start();
        }
        for (int i = 0; i < children.size(); i++) {
            children.get(i).start();
        }
    }

    public void addComponent(Component component) {
        component.className = component.getClass().getSimpleName();
        components.add(component);
        engine.initComponent(component);
        //component.gameObject = this;
    }

    @Override
    public void write(Json json) {
    }

    @Override
    public void read(Json json, JsonValue jsonData) {

    }
}

