package com.pulsar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.pulsar.api.*;
import com.pulsar.audio.Audio;
import com.pulsar.audio.SoundLoader;
import com.pulsar.api.components.Camera;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Statics {

    public static Game main;

    public static SpriteBatch batch;
    public static ShapeDrawer drawer;
    public static Audio audio;
    public static SoundLoader soundLoader;
    public static Gui gui;
    public static Preferences preferences;

    public static Engine engine;
    public static Array<GameObject> allGameObjects = new Array<>();

    public static Json json = new Json();

    public static CameraHolder cameraHolder = new CameraHolder();

    public static FrameBuffer fbo;

    public static Project currentProject;
    public static GameObject selectedGameObject;
    public static Object selection;
    public static GameObject[] selectedGameObjects;

    public static JavaComponentLoader javaComponentLoader;

    public static boolean isGameRunning = false;

    public static final boolean isEngine = true;

    public static int editMode;

    public static com.badlogic.gdx.math.Vector2 mouse = new com.badlogic.gdx.math.Vector2();

    public static ExtendViewport editorViewport = new ExtendViewport(19.2f,10.8f);
    public static OrthographicCamera editorCamera;
    public static Camera gameCamera;;

    public static Array<SerializableGameObject> history = new Array<>();
    public static int historyIdx = 0;

    public static boolean projectChange = false;

    public static Array<String> recentProjects = new Array<>();

}
