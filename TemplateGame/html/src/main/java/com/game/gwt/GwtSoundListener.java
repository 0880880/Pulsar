package com.game.gwt;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.game.audio.SoundListener;

public class GwtSoundListener implements SoundListener {
    @Override
    public SoundListener setPosition(float x, float y, float z) {
        return this;
    }

    @Override
    public SoundListener setPosition(Vector3 v) {
        return this;
    }

    @Override
    public SoundListener setPosition(Camera camera) {
        return this;
    }

    @Override
    public SoundListener setOrientation(Vector3 at, Vector3 up) {
        return this;
    }

    @Override
    public SoundListener setOrientation(Camera camera) {
        return this;
    }

    @Override
    public SoundListener setSpeed(float x, float y, float z) {
        return this;
    }

    @Override
    public SoundListener setSpeed(Vector3 speed) {
        return this;
    }
}
