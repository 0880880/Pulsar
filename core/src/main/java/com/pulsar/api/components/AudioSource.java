package com.pulsar.api.components;

import com.pulsar.Statics;
import com.pulsar.api.AudioManager;
import com.pulsar.api.Component;
import com.pulsar.api.audio.AudioClip;
import com.pulsar.api.math.MathUtils;
import com.pulsar.audio.BufferedSoundSource;

public class AudioSource extends Component {

    public AudioClip audioClip;
    public boolean playOnStart = false;
    public boolean loop = false;

    public float volume = 1;
    public float pitch = 1;

    private BufferedSoundSource source;

    public void start() {

        source = audioClip.getSource();

        AudioManager.sources.add(source);

        if (playOnStart) play();

    }

    public void update() {

        source.setLooping(loop);
        source.setVolume(volume);
        source.setPitch(pitch);

    }

    public void play() {
        source.play();
    }

    public void stop() {
        source.stop();
    }

    public void pause() {
        source.pause();
    }

    public float getPlaybackPosition() {
        return source.getPlaybackPosition();
    }

    public float getDuration() {
        return source.getDuration();
    }

}
