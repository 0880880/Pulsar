package com.pulsar.api;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class SerializableScene
    implements Json.Serializable
{

    public String name;
    public SerializableGameObject rootGameObject;

    public SerializableScene() { }

    public SerializableScene(Scene scene) {
        name = scene.name;
        rootGameObject = new SerializableGameObject(scene.rootGameObject);
    }

    public Scene createScene() {
        Scene scene = new Scene();
        scene.name = name;
        scene.rootGameObject = rootGameObject.createGameObject(null);
        return scene;
    }

    @Override
    public void write(Json json) {
        json.writeValue("name", name);
        json.writeValue("rootGameObject", rootGameObject);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        name = json.readValue("name", String.class, jsonData);
        rootGameObject = json.readValue("rootGameObject", SerializableGameObject.class, jsonData);
    }
}
