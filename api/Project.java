package com.pulsar.api;

import com.pulsar.api.math.Vector2;

import java.io.File;


public class Project {

 public String projectName;


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

 public static Project loadProject(String path) {return null;}


}
