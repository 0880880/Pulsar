package com.pulsar.api;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.pulsar.api.math.Vector2;

import java.io.File;
import java.util.ArrayList;

import static com.game.Statics.json;

public class Project {

    public FileHandle path;
    public String projectName;

    public ArrayList<Class<? extends Component>> components = new ArrayList<>();

    public GameObject rootGameObject;

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

    public Project() {}

    public static Project loadProject(String path) {

        File pFolder = new File(path);

        if (pFolder.isDirectory()) {
            FileHandle projectSave = Gdx.files.absolute(pFolder.getAbsolutePath() + "\\main.prj");
            if (projectSave.exists())
                return json.fromJson(SerializableProject.class, projectSave.readString()).createProject();
        }

        return null;

    }

}

