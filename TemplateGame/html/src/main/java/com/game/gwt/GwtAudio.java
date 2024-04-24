package com.game.gwt;

import com.badlogic.gdx.Gdx;
import com.game.audio.*;

public class GwtAudio implements Audio {

    GwtSoundListener soundListener = new GwtSoundListener();

    @Override
    public void init() {}

    @Override
    public BufferedSoundSource obtainSource(SoundBuffer soundBuffer) {
        if (soundBuffer instanceof GwtSoundBuffer)
            return new GwtBufferedSoundSource(((GwtSoundBuffer) soundBuffer));
        return null;
    }

    @Override
    public SoundListener getListener() {
        return soundListener;
    }

    @Override
    public void stopAll() {

    }

    @Override
    public StreamedSoundSource obtainStreamedSource(SoundBuffer soundBuffer) {
        if (soundBuffer instanceof GwtSoundBuffer)
            return new GwtStreamedSoundSource(((GwtSoundBuffer) soundBuffer));
        return null;
    }
}
