package com.pulsar.api.audio;

import com.badlogic.gdx.files.FileHandle;
import com.pulsar.api.AudioManager;
import com.game.Statics;
import com.game.audio.BufferedSoundSource;
import com.game.Utils;

public class AudioClip
    //implements Json.Serializable
{

    public String soundFile;
    private transient FileHandle file;

    private transient com.game.audio.SoundBuffer buffer;

    public void init() {
        if (soundFile != null) file = Utils.getFile(soundFile);
    }

    public BufferedSoundSource getSource() {
        if (soundFile != null && buffer == null) {
            init();
            buffer = Statics.soundLoader.load(file);
            AudioManager.buffers.add(buffer);
        }
        return Statics.audio.obtainSource(buffer);
    }

    public com.game.audio.StreamedSoundSource getStreamedSource() {
        return Statics.audio.obtainStreamedSource(buffer);
    }

}

