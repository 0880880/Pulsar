package com.pulsar.lwjgl3;

import com.badlogic.gdx.files.FileHandle;
import com.pulsar.audio.SoundBuffer;

public class Lwjgl3SoundBuffer implements SoundBuffer {

    public FileHandle file;
    public de.pottgames.tuningfork.SoundBuffer tuningForkSoundBuffer;

    public Lwjgl3SoundBuffer(FileHandle file, de.pottgames.tuningfork.SoundBuffer tuningForkSoundBuffer) {
        this.file = file;
        this.tuningForkSoundBuffer = tuningForkSoundBuffer;
    }

    @Override
    public void dispose() {
        tuningForkSoundBuffer.dispose();
    }

}
