package com.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LoadingScreen implements Screen {

    private final Game game;

    private SpriteBatch batch;
    private ScreenViewport viewport;

    private BitmapFont font;

    private Vector2 halfTextSize = new Vector2();

    public LoadingScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.WHITE;
        parameter.size = 32;
        parameter.characters = "Clicktosar. ";

        font = generator.generateFont(parameter);

        batch = new SpriteBatch();
        viewport = new ScreenViewport();

        GlyphLayout glyphLayout = new GlyphLayout(font, "Click to start.");
        halfTextSize.set(glyphLayout.width / 2f, glyphLayout.height / 2f);

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0,0,0,1);

        viewport.apply();

        if (Gdx.input.isTouched() && Gdx.app.getType() == Application.ApplicationType.WebGL) {
            game.setScreen(new MainScreen());
        }

        batch.begin();
        font.draw(batch, "Click to start.", Gdx.graphics.getWidth() / 2f - halfTextSize.x, Gdx.graphics.getHeight() / 2f + halfTextSize.x);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }

}
