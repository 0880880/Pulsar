package com.game;

import com.badlogic.gdx.Game;
import com.game.audio.Audio;
import com.game.audio.SoundLoader;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class Main extends Game {

    public Main(Audio audio, SoundLoader soundLoader) {
        Statics.audio = audio;
        Statics.soundLoader = soundLoader;
    }

    @Override
    public void create() {
        setScreen(Gdx.app.getType() == Application.ApplicationType.WebGL ? new LoadingScreen(this) : new MainScreen());
    }
}
