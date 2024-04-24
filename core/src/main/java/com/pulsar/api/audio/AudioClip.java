package com.pulsar.api.audio;

import com.badlogic.gdx.files.FileHandle;
import com.pulsar.api.AudioManager;
import com.pulsar.Statics;
import com.pulsar.audio.BufferedSoundSource;
import com.pulsar.utils.Utils;

public class AudioClip
    //implements Json.Serializable
{

    public String soundFile;
    private transient FileHandle file;

    private transient com.pulsar.audio.SoundBuffer buffer;

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

    public com.pulsar.audio.StreamedSoundSource getStreamedSource() {
        return Statics.audio.obtainStreamedSource(buffer);
    }

}
