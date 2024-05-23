package com.pulsar.api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.pulsar.api.math.Vector2;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static com.game.Statics.*;

public class Project {

    public FileHandle path;
    public String projectName;

    public ArrayList<Class<? extends Component>> components = new ArrayList<>();

    public HashMap<String, Scene> scenes = new HashMap<>();
    public String mainScene;
    public String currentScene;

    public String windowTitle = "Game";
    public String buildName = "Game";
    public String version = "1.0.0";
    public Vector2 windowSize = new Vector2(800, 600);
    public boolean fullscreen = false;

    public String appIconLinux = "";
    public String appIconWin = "";
    public String appIconMac = "";

    public String windowIcon16 = "";
    public String windowIcon32 = "";
    public String windowIcon64 = "";
    public String windowIcon128 = "";

    public boolean physicsEnabled = true;
    public Vector2 physicsGravity = new Vector2(0, -9.81f);
    public int physicsVelocityIterations = 6;
    public int physicsPositionIterations = 2;

    public ArrayList<String> dependencies = new ArrayList<>();
    public ArrayList<String> desktopDependencies = new ArrayList<>();
    public ArrayList<String> htmlDependencies = new ArrayList<>();
    public ArrayList<String> repositories = new ArrayList<>();

    public Project() {
    }

    public static Project loadProject(String path) {

        File pFolder = new File(path);

        if (pFolder.isDirectory()) {
            FileHandle projectSave = Gdx.files.absolute(pFolder.getAbsolutePath() + "\\main.prj");
            currentProjectPath = projectSave.parent();
            if (projectSave.exists()) {
                Project proj = json.fromJson(SerializableProject.class, projectSave.readString()).createProject(projectSave.parent());
                allGameObjects.put(proj.currentScene, new Array<>());
                return proj;
            }
        }

        return null;

    }

    public void changeScene(String scenePath) {
        currentScene = scenePath;
        if (!allGameObjects.containsKey(currentScene)) allGameObjects.put(currentScene, new Array<>());
        getCurrentScene();
    }

    public Scene getScene(String scenePath) {
        if (!allGameObjects.containsKey(scenePath)) allGameObjects.put(scenePath, new Array<>());
        if (!scenes.containsKey(scenePath))
            scenes.put(scenePath, json.fromJson(SerializableScene.class, currentProject.path.child("Assets").child(scenePath)).createScene());
        return scenes.get(scenePath);
    }

    public Scene getCurrentScene() {
        return getScene(currentScene);
    }

}

