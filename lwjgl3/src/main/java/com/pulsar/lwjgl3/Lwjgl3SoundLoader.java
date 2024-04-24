package com.pulsar.lwjgl3;

import com.badlogic.gdx.files.FileHandle;
import com.pulsar.audio.SoundBuffer;
import com.pulsar.audio.SoundLoader;

public class Lwjgl3SoundLoader implements SoundLoader {

    @Override
    public SoundBuffer load(FileHandle file) {
        return new Lwjgl3SoundBuffer(file, de.pottgames.tuningfork.SoundLoader.load(file));
    }

}
