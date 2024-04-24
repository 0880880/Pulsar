package com.pulsar.api;

import com.pulsar.api.math.Vector2;

public class GameObjectCondition {

    public GameObject gameObject;

    public Vector2 position = new Vector2();
    public Vector2 scale = new Vector2();
    public float rotation;

    public GameObjectCondition(GameObject gameObject) {
        this.gameObject = gameObject;
        position.set(gameObject.transform.position);
        scale.set(gameObject.transform.scale);
        rotation = gameObject.transform.rotation;
    }

    public void apply() {
        gameObject.transform.position.set(position);
        gameObject.transform.scale.set(scale);
        gameObject.transform.rotation = rotation;
    }

}

