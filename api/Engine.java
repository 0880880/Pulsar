package com.pulsar.api;

import com.pulsar.api.audio.AudioClip;
import com.pulsar.api.graphics.Material;
import com.pulsar.api.graphics.Shader;
import com.pulsar.api.graphics.TextureAsset;
import com.pulsar.api.math.*;
import com.pulsar.api.physics.Physics;

import java.util.HashMap;

public class Engine {


 TextureAsset defaultTextureAsset;

 public HashMap<String, TextureAsset> loadedTextures = new HashMap<>();
 public HashMap<String, AudioClip> loadedAudioClips = new HashMap<>();
 public HashMap<String, Shader> loadedShaders = new HashMap<>();
 public HashMap<String, Material> loadedMaterials = new HashMap<>();



 public Shader loadShader(String name, String path) {return null;}




 
private  HashMap<Integer, HashMap<Class<? extends Component>, HashMap<String, Object>>> gameObjectsComponentsFieldMap = new HashMap<>();

 private Object copyObject(Object object, Class<?> c) {return null;}

 
void copyComponentsFields(HashMap<Integer, HashMap<Class<? extends Component>, HashMap<String, Object>>> map, GameObject parent) {}

 private void addComponentsFields(HashMap<Integer, HashMap<Class<? extends Component>, HashMap<String, Object>>> map, GameObject parent) {}

 private void addChildrenToICs(GameObject gameObject) {}

 private void applyICsToChildren() {}

 public void start(GameObject main, boolean ic) {}

 public void start() {}

 public void stop() {}

 private float accumulator = 0;
 private  float timeStep = 1/60f;

 public void update(GameObject main) {}

 public void update() {}

 public void initComponent(Component component) {}

 public static void setWindowedMode(int width, int height) {}

 public static void setFullscreen() {}

}
