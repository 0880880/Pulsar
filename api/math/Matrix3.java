package com.pulsar.api.math;


import java.io.Serializable;


public class Matrix3 implements Serializable {
 public static  int M00 = 0;
 public static  int M01 = 3;
 public static  int M02 = 6;
 public static  int M10 = 1;
 public static  int M11 = 4;
 public static  int M12 = 7;
 public static  int M20 = 2;
 public static  int M21 = 5;
 public static  int M22 = 8;
 public float[] val = new float[9];
 private float[] tmp = new float[9];
 {}

 public Matrix3 () {}

 public Matrix3 (Matrix3 matrix) {}

 
 public Matrix3 (float[] values) {}

 
 public Matrix3 idt () {return null;}

 
 
public Matrix3 mul (Matrix3 m) {return null;}

 
 
public Matrix3 mulLeft (Matrix3 m) {return null;}

 
 
public Matrix3 setToRotation (float degrees) {return null;}

 
 
public Matrix3 setToRotationRad (float radians) {return null;}

 
public Matrix3 setToRotation (Vector3 axis, float degrees) {return null;}

 
public Matrix3 setToRotation (Vector3 axis, float cos, float sin) {return null;}

 
 
public Matrix3 setToTranslation (float x, float y) {return null;}

 
 
public Matrix3 setToTranslation (Vector2 translation) {return null;}

 
 
public Matrix3 setToScaling (float scaleX, float scaleY) {return null;}

 
 
public Matrix3 setToScaling (Vector2 scale) {return null;}

 
public String toString () {return null;}

 
 
public float det () {return 0.0f;}

 
 
public Matrix3 inv () {return null;}

 
 
public Matrix3 set (Matrix3 mat) {return null;}

 
 
public Matrix3 set (Affine2 affine) {return null;}

 
 
public Matrix3 set (Matrix4 mat) {return null;}

 
 
public Matrix3 set (float[] values) {return null;}

 
 
public Matrix3 trn (Vector2 vector) {return null;}

 
 
public Matrix3 trn (float x, float y) {return null;}

 
 
public Matrix3 trn (Vector3 vector) {return null;}

 
 
public Matrix3 translate (float x, float y) {return null;}

 
 
public Matrix3 translate (Vector2 translation) {return null;}

 
 
public Matrix3 rotate (float degrees) {return null;}

 
 
public Matrix3 rotateRad (float radians) {return null;}

 
 
public Matrix3 scale (float scaleX, float scaleY) {return null;}

 
 
public Matrix3 scale (Vector2 scale) {return null;}

 
 
public float[] getValues () {return null;}

 
public Vector2 getTranslation (Vector2 position) {return null;}

 
 
public Vector2 getScale (Vector2 scale) {return null;}

 
public float getRotation () {return 0.0f;}

 
public float getRotationRad () {return 0.0f;}

 
 
public Matrix3 scl (float scale) {return null;}

 
 
public Matrix3 scl (Vector2 scale) {return null;}

 
 
public Matrix3 scl (Vector3 scale) {return null;}

 
 
public Matrix3 transpose () {return null;}

 
 
private static void mul (float[] mata, float[] matb) {}
}
