package com.pulsar.api.math;

public class Vector2 {

 public static  Vector2 Zero = new Vector2(0,0);
 public static  Vector2 X = new Vector2(1,0);
 public static  Vector2 Y = new Vector2(0,1);

 public float x;
 public float y;

 public Vector2() {}

 public Vector2(float x, float y) {}

 public Vector2(Vector2 v) {}

 public Vector2 set(float x, float y) {return null;}

 
public Vector2 set(Vector2 v) {return null;}

 
public Vector2 setZero() {return null;}

 
public Vector2 add(float x, float y) {return null;}

 
public Vector2 add(Vector2 v) {return null;}

 
public Vector2 sub(float x, float y) {return null;}

 
public Vector2 sub(Vector2 v) {return null;}

 
public Vector2 scl(float scalar) {return null;}

 
public float len() {return 0.0f;}

 
public float len2() {return 0.0f;}

 
public Vector2 nor() {return null;}

 
public static float dot(float x1, float y1, float x2, float y2) {return 0.0f;}

 
public float dot(Vector2 v) {return 0.0f;}

 
public float dot(float ox, float oy) {return 0.0f;}

 
public static float dst (float x1, float y1, float x2, float y2) {return 0.0f;}

 
public float dst (Vector2 v) {return 0.0f;}

 
public float dst (float x, float y) {return 0.0f;}

 
public static float dst2 (float x1, float y1, float x2, float y2) {return 0.0f;}

 
public float dst2 (Vector2 v) {return 0.0f;}

 
public float dst2 (float x, float y) {return 0.0f;}

 
public float crs (Vector2 v) {return 0.0f;}

 
public float crs (float x, float y) {return 0.0f;}

 
public float angleRad() {return 0.0f;}

 
public float angleDeg() {return 0.0f;}

 
public boolean equals(Object obj) {return false;}

 
public Vector2 cpy() {return null;}

 
public String toString() {return null;}


}
