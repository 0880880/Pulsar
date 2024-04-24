package com.game.lwjgl3;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import de.pottgames.tuningfork.SoundListener;

public class Lwjgl3SoundListener implements com.game.audio.SoundListener {

    public SoundListener tuningForkSoundListener;

    public Lwjgl3SoundListener(SoundListener tuningForkSoundListener) {
        this.tuningForkSoundListener = tuningForkSoundListener;
    }

    @Override
    public com.game.audio.SoundListener setPosition(float x, float y, float z) {
        tuningForkSoundListener.setPosition(x, y, z);
        return this;
    }

    @Override
    public com.game.audio.SoundListener setPosition(Vector3 v) {
        tuningForkSoundListener.setPosition(v);
        return this;
    }

    @Override
    public com.game.audio.SoundListener setPosition(Camera camera) {
        tuningForkSoundListener.setPosition(camera);
        return this;
    }

    @Override
    public com.game.audio.SoundListener setOrientation(Vector3 at, Vector3 up) {
        tuningForkSoundListener.setOrientation(at, up);
        return this;
    }

    @Override
    public com.game.audio.SoundListener setOrientation(Camera camera) {
        tuningForkSoundListener.setOrientation(camera);
        return this;
    }

    @Override
    public com.game.audio.SoundListener setSpeed(float x, float y, float z) {
        tuningForkSoundListener.setSpeed(x, y, z);
        return this;
    }

    @Override
    public com.game.audio.SoundListener setSpeed(Vector3 speed) {
        tuningForkSoundListener.setSpeed(speed);
        return this;
    }
}
