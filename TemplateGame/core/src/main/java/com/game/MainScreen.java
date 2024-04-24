package com.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import static com.game.Statics.*;

public class MainScreen implements Screen {

    @Override
    public void show() {
        audio.init();
        gameObjectManager = new GameObjectManager();
        gameObjectManager.load();
        gameObjectManager.start();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1,1,1,1);
        gameObjectManager.update();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
