package com.game.gwt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.game.audio.StreamedSoundSource;

public class GwtStreamedSoundSource implements StreamedSoundSource {

    Music music;
    float volume;

    public GwtStreamedSoundSource(GwtSoundBuffer soundBuffer) {
        music = Gdx.audio.newMusic(soundBuffer.file);
    }

    @Override
    public void play() {
        music.play();
    }

    @Override
    public void stop() {
        music.stop();
    }

    @Override
    public void pause() {
        music.pause();
    }

    @Override
    public void setLooping(boolean looping) {
        music.setLooping(looping);
    }

    @Override
    public void setVolume(float volume) {
        music.setVolume(volume);
    }

    @Override
    public void setPitch(float pitch) { }

    @Override
    public void setPlaybackPosition(float playbackPosition) {
        music.setPan(playbackPosition, volume);
    }

    @Override
    public boolean isPlaying() {
        return music.isPlaying();
    }

    @Override
    public boolean isPaused() {
        return !music.isPlaying();
    }

    @Override
    public float getDuration() {
        return 0;
    }

    @Override
    public float getPlaybackPosition() {
        return music.getPosition();
    }

    @Override
    public void dispose() {
        music.dispose();
    }
}
