package com.pulsar;

import com.badlogic.gdx.Game;
import com.pulsar.audio.Audio;
import com.pulsar.audio.SoundLoader;
import com.pulsar.utils.Utils;

import static com.pulsar.Statics.preferences;

public class Main extends Game {

	public WindowListener windowListener;

	public Main(Audio audio, SoundLoader soundLoader) {
        Statics.audio = audio;
        Statics.soundLoader = soundLoader;
		windowListener = () -> preferences.flush();
	}

	@Override
	public void create() {
		Statics.main = this;
		Utils.initialize();
		setScreen(new MainMenu());
	}
}
