package com.pulsar.api;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.pulsar.Statics;
import com.pulsar.api.math.Vector2;
import com.pulsar.utils.Utils;

import java.util.HashMap;
import java.util.List;

import static com.pulsar.Statics.*;

public class SerializableProject
    implements Json.Serializable
{

    public com.badlogic.gdx.files.FileHandle path;
    public String projectName;

    public HashMap<String, SerializableScene> scenes = new HashMap<>();
    public String mainScene;

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

    public String[] dependencies = new String[0];
    public String[] desktopDependencies = new String[0];
    public String[] htmlDependencies = new String[0];
    public String[] repositories = new String[0];

    public SerializableProject(Project project) {

        path = project.path;
        projectName = project.projectName;
        for (String key : project.scenes.keySet()) {
            scenes.put(key, new SerializableScene(project.scenes.get(key)));
        }
        mainScene = project.mainScene;

        windowTitle = project.windowTitle;
        buildName = project.buildName;
        version = project.version;
        windowSize.set(project.windowSize);
        fullscreen = project.fullscreen;

        appIconLinux = project.appIconLinux;
        appIconWin = project.appIconWin;
        appIconMac = project.appIconMac;

        windowIcon16 = project.windowIcon16;
        windowIcon32 = project.windowIcon32;
        windowIcon64 = project.windowIcon64;
        windowIcon128 = project.windowIcon128;

        physicsEnabled = project.physicsEnabled;
        physicsGravity = project.physicsGravity;
        physicsVelocityIterations = project.physicsVelocityIterations;
        physicsPositionIterations = project.physicsPositionIterations;

        dependencies = new String[project.dependencies.size()]; // CORE
        for (int i = 0; i < dependencies.length; i++) dependencies[i] = project.dependencies.get(i);
        desktopDependencies = new String[project.desktopDependencies.size()]; // DESKTOP
        for (int i = 0; i < desktopDependencies.length; i++) desktopDependencies[i] = project.desktopDependencies.get(i);
        htmlDependencies = new String[project.htmlDependencies.size()]; // HTML
        for (int i = 0; i < htmlDependencies.length; i++) htmlDependencies[i] = project.htmlDependencies.get(i);
        repositories = new String[project.repositories.size()];
        for (int i = 0; i < repositories.length; i++) repositories[i] = project.repositories.get(i);

    }

    public SerializableProject() {
    }

    public Project createProject(com.badlogic.gdx.files.FileHandle folder) {
        currentProjectPath = folder;
        Project project = new Project();
        project.path = folder;
        project.projectName = projectName;

        project.windowTitle = windowTitle;
        project.buildName = buildName;
        project.version = version;
        project.windowSize.set(windowSize);
        project.fullscreen = fullscreen;

        project.appIconLinux = appIconLinux;
        project.appIconWin = appIconWin;
        project.appIconMac = appIconMac;

        project.windowIcon16 = windowIcon16;
        project.windowIcon32 = windowIcon32;
        project.windowIcon64 = windowIcon64;
        project.windowIcon128 = windowIcon128;

        project.physicsEnabled = physicsEnabled;
        project.physicsGravity = physicsGravity;
        project.physicsVelocityIterations = physicsVelocityIterations;
        project.physicsPositionIterations = physicsPositionIterations;

        Statics.currentProject = project;
        Utils.compile();

        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            com.badlogic.gdx.files.FileHandle[] assets = currentProject.path.child("assets").list();
            for (com.badlogic.gdx.files.FileHandle file : assets) {
                if (file.extension().equalsIgnoreCase("java")) {
                    if (!file.readString().isEmpty()) {
                        if (!currentProject.path.child("Temp").child(file.nameWithoutExtension() + ".jar").exists())
                            continue;
                        Class<?> c = Utils.getClass(file.nameWithoutExtension());
                        if (ClassReflection.isAssignableFrom(Component.class, c)) {
                            Class<? extends Component> nc = (Class<? extends Component>) c;
                            currentProject.components.add(nc);
                        }
                    }
                }
            }
        }

        project.mainScene = mainScene;
        project.currentScene = project.mainScene;

        project.dependencies.addAll(List.of(dependencies));
        project.desktopDependencies.addAll(List.of(desktopDependencies));
        project.htmlDependencies.addAll(List.of(htmlDependencies));
        project.repositories.clear();
        project.repositories.add("https://repo.maven.apache.org/maven2/");
        project.repositories.addAll(List.of(repositories));

        return project;
    }

    @Override
    public void write(Json json) {

        json.writeValue("path", path.path());
        json.writeValue("projectName", projectName);

        json.writeValue("mainScene", mainScene);

        json.writeValue("windowTitle", windowTitle);
        json.writeValue("buildName", buildName);
        json.writeValue("version", version);
        json.writeValue("windowSize", windowSize);
        json.writeValue("fullscreen", fullscreen);

        json.writeValue("appIconLinux", appIconLinux);
        json.writeValue("appIconWin", appIconWin);
        json.writeValue("appIconMac", appIconMac);

        json.writeValue("windowIcon16", windowIcon16);
        json.writeValue("windowIcon32", windowIcon32);
        json.writeValue("windowIcon64", windowIcon64);
        json.writeValue("windowIcon128", windowIcon128);

        json.writeValue("physicsEnabled", physicsEnabled);
        json.writeValue("physicsGravity", physicsGravity);
        json.writeValue("physicsVelocityIterations", physicsVelocityIterations);
        json.writeValue("physicsPositionIterations", physicsPositionIterations);

        json.writeValue("dependencies", dependencies);
        json.writeValue("desktopDependencies", desktopDependencies);
        json.writeValue("htmlDependencies", htmlDependencies);
        json.writeValue("repositories", repositories);

    }

    public String toJson() {
        for (String key : scenes.keySet()) {
            String jsonCode = json.toJson(scenes.get(key));
            path.child("Assets").child(key).writeString(jsonCode, false);
        }

        return json.toJson(this);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        if (Gdx.files != null && Gdx.app.getType() != Application.ApplicationType.WebGL) path = Gdx.files.absolute(json.readValue("path", String.class, jsonData));
        projectName = json.readValue("projectName", String.class, jsonData);

        mainScene = json.readValue("mainScene", String.class, jsonData);

        windowTitle = json.readValue("windowTitle", String.class, jsonData);
        buildName = json.readValue("buildName", String.class, jsonData);
        version = json.readValue("version", String.class, jsonData);
        windowSize = json.readValue("windowSize", Vector2.class, jsonData);
        fullscreen = json.readValue("fullscreen", boolean.class, jsonData);

        appIconLinux = json.readValue("appIconLinux", String.class, jsonData);
        appIconWin = json.readValue("appIconWin", String.class, jsonData);
        appIconMac = json.readValue("appIconMac", String.class, jsonData);

        windowIcon16 = json.readValue("windowIcon16", String.class, jsonData);
        windowIcon32 = json.readValue("windowIcon32", String.class, jsonData);
        windowIcon64 = json.readValue("windowIcon64", String.class, jsonData);
        windowIcon128 = json.readValue("windowIcon128", String.class, jsonData);

        physicsEnabled = json.readValue("physicsEnabled", boolean.class, jsonData);
        physicsGravity = json.readValue("physicsGravity", Vector2.class, jsonData);
        physicsVelocityIterations = json.readValue("physicsVelocityIterations", int.class, jsonData);
        physicsPositionIterations = json.readValue("physicsPositionIterations", int.class, jsonData);

        dependencies = json.readValue("dependencies", String[].class, jsonData);
        desktopDependencies = json.readValue("desktopDependencies", String[].class, jsonData);
        htmlDependencies = json.readValue("htmlDependencies", String[].class, jsonData);
        repositories = json.readValue("repositories", String[].class, jsonData);

    }
}
