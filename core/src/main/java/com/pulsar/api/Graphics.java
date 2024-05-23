package com.pulsar.api;

import com.badlogic.gdx.Gdx;
import com.pulsar.Statics;

public class Graphics {

    public enum Cursor {
        None, Arrow, Ibeam, Crosshair, Hand, HorizontalResize, VerticalResize, NWSEResize, NESWResize, AllResize, NotAllowed
    }

    private static float x;
    private static float y;

    private static int width;
    private static int height;

    static void init() {

        width = (int) Statics.currentProject.windowSize.x;
        height = (int) Statics.currentProject.windowSize.y;

    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setWindowMode(int width, int height) {
        Gdx.graphics.setWindowedMode(width, height);
    }

    public static void setFullscreenMode() {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
    }

    public static boolean isFullscreen() {
        return Gdx.graphics.isFullscreen();
    }

    public static void setTitle(String title) {
        Gdx.graphics.setTitle(title);
    }

    public static void setResizable(boolean resizable) {
        Gdx.graphics.setResizable(resizable);
    }

    public static void setUndecorated(boolean undecorated) {
        Gdx.graphics.setUndecorated(undecorated);
    }

    public static void setVSync(boolean vsync) {
        Gdx.graphics.setVSync(vsync);
    }

    public static void setForegroundFPS(int fps) {
        Gdx.graphics.setForegroundFPS(fps);
    }

    public static void setCursor(Cursor cursor) {
        com.badlogic.gdx.graphics.Cursor.SystemCursor sc = com.badlogic.gdx.graphics.Cursor.SystemCursor.None;
        switch (cursor) {
            case Arrow:
                sc = com.badlogic.gdx.graphics.Cursor.SystemCursor.Arrow;break;
            case Ibeam:
                sc = com.badlogic.gdx.graphics.Cursor.SystemCursor.Ibeam;break;
            case Crosshair:
                sc = com.badlogic.gdx.graphics.Cursor.SystemCursor.Crosshair;break;
            case Hand:
                sc = com.badlogic.gdx.graphics.Cursor.SystemCursor.Hand;break;
            case HorizontalResize:
                sc = com.badlogic.gdx.graphics.Cursor.SystemCursor.HorizontalResize;break;
            case VerticalResize:
                sc = com.badlogic.gdx.graphics.Cursor.SystemCursor.VerticalResize;break;
            case NWSEResize:
                sc = com.badlogic.gdx.graphics.Cursor.SystemCursor.NWSEResize;break;
            case NESWResize:
                sc = com.badlogic.gdx.graphics.Cursor.SystemCursor.NESWResize;break;
            case AllResize:
                sc = com.badlogic.gdx.graphics.Cursor.SystemCursor.AllResize;break;
            case NotAllowed:
                sc = com.badlogic.gdx.graphics.Cursor.SystemCursor.NotAllowed;break;
        }
        Gdx.graphics.setSystemCursor(sc);
    }

    public void changeScene(String path) {
        Statics.currentProject.changeScene(path);
    }

}
