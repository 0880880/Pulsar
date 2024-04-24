package com.game.lwjgl3;

import com.badlogic.gdx.files.FileHandle;
import com.game.audio.SoundBuffer;
import de.pottgames.tuningfork.decoder.AudioStream;

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
