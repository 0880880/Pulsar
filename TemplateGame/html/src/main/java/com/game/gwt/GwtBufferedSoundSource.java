package com.game.gwt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.game.audio.BufferedSoundSource;

public class GwtBufferedSoundSource implements BufferedSoundSource {

    Sound sound;
    long soundId;
    float volume;

    public GwtBufferedSoundSource(GwtSoundBuffer gwtSoundBuffer) {
        this.sound = gwtSoundBuffer.sound;
    }

    @Override
    public void play() {
        soundId = sound.play();
    }

    @Override
    public void stop() {
        sound.stop(soundId);
    }

    @Override
    public void pause() {
        sound.pause(soundId);
    }

    @Override
    public void setLooping(boolean looping) {
        sound.setLooping(soundId, looping);
    }

    @Override
    public void setVolume(float volume) {
        this.volume = volume;
        sound.setVolume(soundId, volume);
    }

    @Override
    public void setPitch(float pitch) {
        sound.setPitch(soundId, pitch);
    }

    @Override
    public void setPlaybackPosition(float playbackPosition) {
        sound.setPan(soundId, playbackPosition, volume);
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public boolean isPaused() {
        return false;
    }

    @Override
    public float getDuration() {
        return 0;
    }

    @Override
    public float getPlaybackPosition() {
        return 0;
    }

    @Override
    public void dispose() {}
}
