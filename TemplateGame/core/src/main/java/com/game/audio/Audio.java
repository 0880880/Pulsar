package com.game.audio;

public interface Audio {

    void init();

    BufferedSoundSource obtainSource(SoundBuffer soundBuffer);

    SoundListener getListener();

    void stopAll();

    StreamedSoundSource obtainStreamedSource(SoundBuffer soundBuffer);

}
