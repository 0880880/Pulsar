package com.pulsar.audio;

public interface SongSource
    extends com.badlogic.gdx.utils.Disposable
{

    void play();
    void stop();
    void pause();

    void setLooping(boolean looping);
    void setVolume(float volume);
    void setPitch(float pitch);
    void setPlaybackPosition(float playbackPosition);

    boolean isPlaying();
    boolean isPaused();
    float getDuration();
    float getPlaybackPosition();

}
