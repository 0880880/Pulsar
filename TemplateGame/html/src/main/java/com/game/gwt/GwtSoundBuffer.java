package com.game.gwt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.game.audio.SoundBuffer;

public class GwtSoundBuffer implements SoundBuffer {

    public FileHandle file;
    public Sound sound;

    public GwtSoundBuffer(FileHandle file) {
        this.file = file;
        this.sound = Gdx.audio.newSound(file);
    }


    @Override
    public void dispose() {
        sound.dispose();
    }

}
