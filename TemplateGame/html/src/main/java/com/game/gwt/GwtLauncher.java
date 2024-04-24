package com.game.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.game.Main;
import com.game.gwt.GwtAudio;
import com.game.gwt.GwtSoundLoader;
import com.badlogic.gdx.graphics.g2d.freetype.gwt.FreetypeInjector;

public class GwtLauncher extends GwtApplication {

    @Override
    public void onModuleLoad() {
        FreetypeInjector.inject(GwtLauncher.super::onModuleLoad);
    }
    @Override
    public GwtApplicationConfiguration getConfig () {
        // Resizable application, uses available space in browser with no padding:
        GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(true);
        cfg.padVertical = 0;
        cfg.padHorizontal = 0;
        return cfg;
        // If you want a fixed size application, comment out the above resizable section,
        // and uncomment below:
        //return new GwtApplicationConfiguration(640, 480);
    }

    @Override
    public ApplicationListener createApplicationListener () {
        return new Main(new GwtAudio(), new GwtSoundLoader());
    }
}
