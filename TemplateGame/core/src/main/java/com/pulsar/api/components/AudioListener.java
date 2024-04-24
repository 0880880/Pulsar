package com.pulsar.api.components;

import com.game.Statics;
import com.pulsar.api.Component;
import com.game.audio.SoundListener;

public class AudioListener extends Component {

    private SoundListener soundListener;

    private Camera camera;

    public void start() {

        soundListener = Statics.audio.getListener();

        camera = gameObject.hasComponent(Camera.class) ? gameObject.getComponent(Camera.class) : null;

    }

    public void update() {

        if (camera != null) {
            soundListener.setPosition(camera.getViewport().getCamera()).setOrientation(camera.getViewport().getCamera());
        }

    }

}

