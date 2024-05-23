package com.pulsar.api.audio;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.pulsar.api.AudioManager;
import com.pulsar.Statics;
import com.pulsar.audio.BufferedSoundSource;
import com.pulsar.utils.Utils;

public class AudioClip
    implements Json.Serializable
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

    @Override public void write(Json json) {
        json.writeValue("soundFile", soundFile.substring(Statics.currentProjectPath.path().length()));
    }

    @Override public void read(Json json, JsonValue jsonData) {
        soundFile = Statics.currentProjectPath.path() + json.readValue("soundFile", String.class, jsonData);
    }
}
