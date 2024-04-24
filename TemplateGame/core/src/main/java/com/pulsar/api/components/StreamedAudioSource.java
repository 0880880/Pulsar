package com.pulsar.api.components;

import com.pulsar.api.AudioManager;
import com.pulsar.api.Component;
import com.pulsar.api.audio.AudioClip;
import com.game.audio.StreamedSoundSource;

public class StreamedAudioSource extends Component {

    public AudioClip audioClip;
    public boolean playOnStart = false;
    public boolean loop = false;

    public float volume;
    public float pitch;

    private StreamedSoundSource source;

    public void start() {

        source = audioClip.getStreamedSource();

        AudioManager.sources.add(source);

        if (playOnStart) source.play();

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

