package com.pulsar.api;

import com.pulsar.api.components.Camera;
import com.pulsar.api.math.Vector2;

import java.util.HashMap;

public class Input {

 private static HashMap<String, Integer> keyMapping = new HashMap<>();
 private static HashMap<String, Integer> buttonMapping = new HashMap<>();

 private static Vector2 mousePosition = new Vector2();

 static void init() {}

 public static boolean isKeyPressed(String key) {return false;}

 
public static boolean isKeyJustPressed(String key) {return false;}

 
public static boolean isButtonPressed(String button) {return false;}

 
public static boolean isButtonJustPressed(String button) {return false;}

 
static void update(Camera camera) {}

 public static Vector2 getMouse() {return null;}


}
