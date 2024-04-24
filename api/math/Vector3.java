package com.pulsar.api.math;

public class Vector3 {

 public static  Vector3 Zero = new Vector3(0,0,0);
 public static  Vector3 X = new Vector3(1,0,0);
 public static  Vector3 Y = new Vector3(0,1,0);
 public static  Vector3 Z = new Vector3(0,0,1);

 public float x;
 public float y;
 public float z;

 public Vector3() {}

 public Vector3(float x, float y, float z) {}

 public Vector3(Vector3 v) {}

 public Vector3 set(float x, float y, float z) {return null;}

 
public Vector3 set(Vector3 v) {return null;}

 
public Vector3 setZero() {return null;}

 
public Vector3 add(float x, float y, float z) {return null;}

 
public Vector3 add(Vector3 v) {return null;}

 
public Vector3 sub(float x, float y, float z) {return null;}

 
public Vector3 sub(Vector3 v) {return null;}

 
public Vector3 scl(float scalar) {return null;}

 
public static float len( float x,  float y,  float z) {return 0.0f;}

 
public float len() {return 0.0f;}

 
public static float len2( float x,  float y,  float z) {return 0.0f;}

 
public float len2() {return 0.0f;}

 
public static float dst( float x1,  float y1,  float z1,  float x2,  float y2,  float z2) {return 0.0f;}

 
public float dst( Vector3 vector) {return 0.0f;}

 
public float dst(float x, float y, float z) {return 0.0f;}

 
public static float dst2( float x1,  float y1,  float z1,  float x2,  float y2,  float z2) {return 0.0f;}

 
public float dst2(Vector3 point) {return 0.0f;}

 
public float dst2(float x, float y, float z) {return 0.0f;}

 
public Vector3 nor() {return null;}

 
public static float dot(float x1, float y1, float z1, float x2, float y2, float z2) {return 0.0f;}

 
public float dot( Vector3 vector) {return 0.0f;}

 
public float dot(float x, float y, float z) {return 0.0f;}

 
public Vector3 crs( Vector3 vector) {return null;}

 
public Vector3 crs(float x, float y, float z) {return null;}

 
public boolean equals(Object obj) {return false;}

 
public Vector3 cpy() {return null;}


}
