package com.pulsar.utils;

import com.pulsar.api.GameObject;

public interface GameObjectPreset {

    GameObject create(String name, GameObject parent);

}
