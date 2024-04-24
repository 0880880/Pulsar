package com.pulsar.api;

import com.badlogic.gdx.Gdx;

public class Time {

    public static float getDeltaTime() {
        return Gdx.graphics.getDeltaTime();
    }

    public static float getFramesPerSecond() {
        return Gdx.graphics.getFramesPerSecond();
    }

}

