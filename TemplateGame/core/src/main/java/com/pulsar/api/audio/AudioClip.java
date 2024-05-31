package com.pulsar.api.audio;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.pulsar.api.AudioManager;
import com.game.Statics;
import com.game.audio.BufferedSoundSource;
import com.game.Utils;

public class AudioClip
    implements Json.Serializable
{

    public String soundFile;
    private transient com.badlogic.gdx.files.FileHandle file;

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

    @Override public void write(Json json) {
        json.writeValue("soundFile", soundFile.substring(Statics.currentProjectPath.path().length()));
    }

    @Override public void read(Json json, JsonValue jsonData) {
        soundFile = Statics.currentProjectPath.path() + json.readValue("soundFile", String.class, jsonData);
    }
}

