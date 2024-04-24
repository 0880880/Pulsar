package com.pulsar.audio;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

public interface SoundListener {

    SoundListener setPosition(float x, float y, float z);
    SoundListener setPosition(Vector3 v);
    SoundListener setPosition(Camera camera);

    SoundListener setOrientation(Vector3 at, Vector3 up);
    SoundListener setOrientation(Camera camera);

    SoundListener setSpeed(float x, float y, float z);
    SoundListener setSpeed(Vector3 speed);

}
