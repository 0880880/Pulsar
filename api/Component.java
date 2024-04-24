package com.pulsar.api;

import com.pulsar.api.components.Transform;
import com.pulsar.api.physics.Collision;

public abstract class Component {

 String className;
 Engine engine;

 public GameObject gameObject;


 private static String getPackageName(Class<?> cls) {return null;}

 
private static String componentsPackageName = getPackageName(Transform.class);

 public static boolean isComponentBuiltin(Class<?> componentClass) {return false;}

 
void initialize(Engine engine) {}

 public void start() {}

 public void awake() {}

 public void update() {}

 public void debugUpdate() {}

 public void fixedUpdate() {}

 public void lateUpdate() {}

 public void onEnterCollision(Collision collision) {}

 public void onLeaveCollision(Collision collision) {}

}
