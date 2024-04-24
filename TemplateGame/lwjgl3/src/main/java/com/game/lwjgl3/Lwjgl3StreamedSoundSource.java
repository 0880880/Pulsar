package com.game.lwjgl3;

import com.game.audio.StreamedSoundSource;

public class Lwjgl3StreamedSoundSource implements StreamedSoundSource {

    public de.pottgames.tuningfork.StreamedSoundSource tuningForkSource;

    public Lwjgl3StreamedSoundSource(de.pottgames.tuningfork.StreamedSoundSource tuningForkSource) {
        this.tuningForkSource = tuningForkSource;
    }

    @Override
    public void play() {
        tuningForkSource.play();
    }

    @Override
    public void stop() {
        tuningForkSource.stop();
    }

    @Override
    public void pause() {
        tuningForkSource.pause();
    }

    @Override
    public void setLooping(boolean looping) {
        tuningForkSource.setLooping(looping);
    }

    @Override
    public void setVolume(float volume) {
        tuningForkSource.setVolume(volume);
    }

    @Override
    public void setPitch(float pitch) {
        tuningForkSource.setPitch(pitch);
    }

    @Override
    public void setPlaybackPosition(float playbackPosition) {
        tuningForkSource.setPlaybackPosition(playbackPosition);
    }

    @Override
    public boolean isPlaying() {
        return tuningForkSource.isPlaying();
    }

    @Override
    public boolean isPaused() {
        return tuningForkSource.isPaused();
    }

    @Override
    public float getDuration() {
        return tuningForkSource.getDuration();
    }

    @Override
    public float getPlaybackPosition() {
        return tuningForkSource.getPlaybackPosition();
    }

    @Override
    public void dispose() {
        tuningForkSource.dispose();
    }
}
