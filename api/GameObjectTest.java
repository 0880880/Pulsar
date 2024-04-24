package com.pulsar.api;

import com.pulsar.api.components.Transform;



public class GameObjectTest implements Cloneable, Json.Serializable {

    public String name;



    public GameObjectTest parent;

    public Transform transform = new Transform();

    public boolean renameMode = false; // Don't add this to user API

    public GameObjectTest() {}

    public GameObjectTest(String name) {}

    public void start() {}

    public void addComponent(Component component) {}

    @Override
    public void write(Json json) {}

    @Override
    public void read(Json json, JsonValue jsonData) {}
}

