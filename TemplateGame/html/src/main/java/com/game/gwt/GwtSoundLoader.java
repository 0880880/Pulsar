package com.game.gwt;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.game.audio.BufferedSoundSource;
import com.game.audio.SoundBuffer;
import com.game.audio.SoundLoader;

public class GwtSoundLoader implements SoundLoader {

    @Override
    public SoundBuffer load(FileHandle file) {
        return new GwtSoundBuffer(file);
    }

}
