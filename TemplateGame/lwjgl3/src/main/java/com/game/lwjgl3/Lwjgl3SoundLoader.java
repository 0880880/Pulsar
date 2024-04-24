package com.game.lwjgl3;

import com.badlogic.gdx.files.FileHandle;
import com.game.audio.SoundBuffer;
import com.game.audio.SoundLoader;

public class Lwjgl3SoundLoader implements SoundLoader {

    @Override
    public SoundBuffer load(FileHandle file) {
        return new Lwjgl3SoundBuffer(file, de.pottgames.tuningfork.SoundLoader.load(file));
    }

}
