package com.pulsar.api;

import com.pulsar.api.math.Vector2;

import java.util.HashMap;
import java.util.List;


public class SerializableProject
{

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

 public SerializableProject(Project project) {}

 public SerializableProject() {}


 @Override


 @Override
}
