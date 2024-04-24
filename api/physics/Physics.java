package com.pulsar.api.physics;

import com.pulsar.api.Component;
import com.pulsar.api.GameObject;
import com.pulsar.api.components.Collider;
import com.pulsar.api.math.Vector2;

import java.util.HashMap;

public class Physics {


 public static boolean enabled = true;
 public static  Vector2 gravity = new Vector2(0, -9.81f);
 public static int velocityIterations = 6;
 public static int positionIterations = 2;

 public static int colliderCounter = 0;

 private static  HashMap<Integer, Collider> colliders = new HashMap<>();

 public static void start() {}

 private interface OnCollisionFunction {}

 public interface RayCastCallback {}

 public interface QueryCallback {}



 public static void stop() {}


 public static void step(float timeStep) {}




 public static void rayCast(RayCastCallback callback, float x0, float y0, float x1, float y1) {}

 public static void queryAABB(QueryCallback callback, float x0, float y0, float x1, float y1) {}

}
