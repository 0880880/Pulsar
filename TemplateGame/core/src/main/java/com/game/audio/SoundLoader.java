package com.game.audio;

import com.badlogic.gdx.files.FileHandle;

public interface SoundLoader {

    SoundBuffer load(FileHandle file);

}
