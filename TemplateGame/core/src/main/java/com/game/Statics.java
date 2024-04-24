package com.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;

import com.pulsar.api.Project;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.pulsar.api.CameraHolder;
import com.pulsar.api.Engine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.pulsar.api.GameObject;
import com.pulsar.api.components.Camera;
import com.game.audio.Audio;
import com.game.audio.SoundLoader;

public class Statics {

    public static Vector2 mouse = new Vector2();

    public static Project currentProject;

    public static Engine engine;

    public static OrthographicCamera editorCamera;

    public static Json json = new Json();

    public static Audio audio;
    public static SoundLoader soundLoader;

    public static Array<GameObject> allGameObjects = new Array<>();

    public static CameraHolder cameraHolder = new CameraHolder();

    public static boolean isGameRunning = true;

    public static GameObjectManager gameObjectManager;

    public static final boolean isEngine = false;

}
