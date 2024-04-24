package com.game.lwjgl3;

import com.game.audio.*;

public class Lwjgl3Audio implements Audio {

    de.pottgames.tuningfork.Audio tuningForkAudio;
    Lwjgl3SoundListener soundListener;

    public Lwjgl3Audio() {
    }

    @Override
    public void init() {
        this.tuningForkAudio = de.pottgames.tuningfork.Audio.init();
        soundListener = new Lwjgl3SoundListener(tuningForkAudio.getListener());
    }

    @Override
    public BufferedSoundSource obtainSource(SoundBuffer soundBuffer) {
        if (soundBuffer instanceof Lwjgl3SoundBuffer)
            return new Lwjgl3BufferedSoundSource(tuningForkAudio.obtainSource(((Lwjgl3SoundBuffer) soundBuffer).tuningForkSoundBuffer));
        return null;
    }

    @Override
    public SoundListener getListener() {
        return soundListener;
    }

    @Override
    public void stopAll() {
        tuningForkAudio.stopAll();
    }

    @Override
    public StreamedSoundSource obtainStreamedSource(SoundBuffer soundBuffer) {
        if (soundBuffer instanceof Lwjgl3SoundBuffer)
            return new Lwjgl3StreamedSoundSource(new de.pottgames.tuningfork.StreamedSoundSource(((Lwjgl3SoundBuffer) soundBuffer).file));
        return null;
    }
}
