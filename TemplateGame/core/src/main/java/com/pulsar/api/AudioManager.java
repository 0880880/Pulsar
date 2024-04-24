package com.pulsar.api;

import com.badlogic.gdx.utils.Array;
import com.game.Statics;
import com.game.audio.SongSource;
import com.game.audio.SoundBuffer;

public class AudioManager {

    public static Array<SongSource> sources = new Array<>();
    public static Array<SoundBuffer> buffers = new Array<>();

    public static void destroySources() {
        Statics.audio.stopAll();
        for (int i = 0; i < sources.size; i++) {
            SongSource source = sources.get(i);
            source.dispose();
        }
        sources.clear();
    }

    public static void dispose() {
        destroySources();
        for (int i = 0; i < buffers.size; i++) {
            SoundBuffer buffer = buffers.get(i);
            buffer.dispose();
        }
        sources.clear();
        buffers.clear();
    }

}

