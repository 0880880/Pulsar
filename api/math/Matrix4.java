package com.pulsar.api.math;

import java.io.Serializable;


public class Matrix4 implements Serializable {
 private static  long serialVersionUID = -2717655254359579617L;
 
 public static  int M00 = 0;
 
 public static  int M01 = 4;
 
 public static  int M02 = 8;
 
 public static  int M03 = 12;
 
 public static  int M10 = 1;
 
 public static  int M11 = 5;
 
 public static  int M12 = 9;
 
 public static  int M13 = 13;
 
 public static  int M20 = 2;
 
 public static  int M21 = 6;
 
 public static  int M22 = 10;
 
 public static  int M23 = 14;
 
 public static  int M30 = 3;
 
 public static  int M31 = 7;
 
 public static  int M32 = 11;
 
 public static  int M33 = 15;

 static  Quaternion quat = new Quaternion();
 static  Quaternion quat2 = new Quaternion();
 static  Vector3 l_vez = new Vector3();
 static  Vector3 l_vex = new Vector3();
 static  Vector3 l_vey = new Vector3();
 static  Vector3 tmpVec = new Vector3();
 static  Matrix4 tmpMat = new Matrix4();
 static  Vector3 right = new Vector3();
 static  Vector3 tmpForward = new Vector3();
 static  Vector3 tmpUp = new Vector3();

 public  float val[] = new float[16];

 
 public Matrix4 () {}

 
 public Matrix4 (Matrix4 matrix) {}

 
 public Matrix4 (float[] values) {}

 
 public Matrix4 (Quaternion quaternion) {}

 
 public Matrix4 (Vector3 position, Quaternion rotation, Vector3 scale) {}

 
 public Matrix4 set (Matrix4 matrix) {return null;}

 
 
public Matrix4 set (float[] values) {return null;}

 
 
public Matrix4 set (Quaternion quaternion) {return null;}

 
 
public Matrix4 set (float quaternionX, float quaternionY, float quaternionZ, float quaternionW) {return null;}

 
 
public Matrix4 set (Vector3 position, Quaternion orientation) {return null;}

 
 
public Matrix4 set (float translationX, float translationY, float translationZ, float quaternionX, float quaternionY,
 float quaternionZ, float quaternionW) {return null;}

 
 
public Matrix4 set (Vector3 position, Quaternion orientation, Vector3 scale) {return null;}

 
 
public Matrix4 set (float translationX, float translationY, float translationZ, float quaternionX, float quaternionY,
 float quaternionZ, float quaternionW, float scaleX, float scaleY, float scaleZ) {return null;}

 
 
public Matrix4 set (Vector3 xAxis, Vector3 yAxis, Vector3 zAxis, Vector3 pos) {return null;}

 
 
public Matrix4 cpy () {return null;}

 
 
public Matrix4 trn (Vector3 vector) {return null;}

 
 
public Matrix4 trn (float x, float y, float z) {return null;}

 
 
public float[] getValues () {return null;}

 
 
public Matrix4 mul (Matrix4 matrix) {return null;}

 
 
public Matrix4 mulLeft (Matrix4 matrix) {return null;}

 
 
public Matrix4 tra () {return null;}

 
 
public Matrix4 idt () {return null;}

 
 
public Matrix4 inv () {return null;}

 
 
public float det () {return 0.0f;}

 
 
public float det3x3 () {return 0.0f;}

 
 
public Matrix4 setToProjection (float near, float far, float fovy, float aspectRatio) {return null;}

 
 
public Matrix4 setToProjection (float left, float right, float bottom, float top, float near, float far) {return null;}

 
 
public Matrix4 setToOrtho2D (float x, float y, float width, float height) {return null;}

 
 
public Matrix4 setToOrtho2D (float x, float y, float width, float height, float near, float far) {return null;}

 
 
public Matrix4 setToOrtho (float left, float right, float bottom, float top, float near, float far) {return null;}

 
 
public Matrix4 setTranslation (Vector3 vector) {return null;}

 
 
public Matrix4 setTranslation (float x, float y, float z) {return null;}

 
 
public Matrix4 setToTranslation (Vector3 vector) {return null;}

 
 
public Matrix4 setToTranslation (float x, float y, float z) {return null;}

 
 
public Matrix4 setToTranslationAndScaling (Vector3 translation, Vector3 scaling) {return null;}

 
 
public Matrix4 setToTranslationAndScaling (float translationX, float translationY, float translationZ, float scalingX,
 float scalingY, float scalingZ) {return null;}

 
 
public Matrix4 setToRotation (Vector3 axis, float degrees) {return null;}

 
 
public Matrix4 setToRotationRad (Vector3 axis, float radians) {return null;}

 
 
public Matrix4 setToRotation (float axisX, float axisY, float axisZ, float degrees) {return null;}

 
 
public Matrix4 setToRotationRad (float axisX, float axisY, float axisZ, float radians) {return null;}

 
 
public Matrix4 setToRotation ( Vector3 v1,  Vector3 v2) {return null;}

 
 
public Matrix4 setToRotation ( float x1,  float y1,  float z1,  float x2,  float y2,  float z2) {return null;}

 
 
public Matrix4 setFromEulerAngles (float yaw, float pitch, float roll) {return null;}

 
 
public Matrix4 setFromEulerAnglesRad (float yaw, float pitch, float roll) {return null;}

 
 
public Matrix4 setToScaling (Vector3 vector) {return null;}

 
 
public Matrix4 setToScaling (float x, float y, float z) {return null;}

 
 
public Matrix4 setToLookAt (Vector3 direction, Vector3 up) {return null;}

 
 
public Matrix4 setToLookAt (Vector3 position, Vector3 target, Vector3 up) {return null;}


 
 
public Matrix4 lerp (Matrix4 matrix, float alpha) {return null;}

 
 
public Matrix4 avg (Matrix4 other, float w) {return null;}

 
 
public Matrix4 avg (Matrix4[] t) {return null;}

 
 
public Matrix4 avg (Matrix4[] t, float[] w) {return null;}

 
 
public Matrix4 set (Matrix3 mat) {return null;}

 
 
public Matrix4 set (Affine2 affine) {return null;}

 
 
public Matrix4 setAsAffine (Affine2 affine) {return null;}

 
 
public Matrix4 setAsAffine (Matrix4 mat) {return null;}

 
public Matrix4 scl (Vector3 scale) {return null;}

 
public Matrix4 scl (float x, float y, float z) {return null;}

 
public Matrix4 scl (float scale) {return null;}

 
public Vector3 getTranslation (Vector3 position) {return null;}

 
 
public Quaternion getRotation (Quaternion rotation, boolean normalizeAxes) {return null;}

 
 
public Quaternion getRotation (Quaternion rotation) {return null;}

 
 
public float getScaleXSquared () {return 0.0f;}

 
 
public float getScaleYSquared () {return 0.0f;}

 
 
public float getScaleZSquared () {return 0.0f;}

 
 
public float getScaleX () {return 0.0f;}

 
 
public float getScaleY () {return 0.0f;}

 
 
public float getScaleZ () {return 0.0f;}

 
 
public Vector3 getScale (Vector3 scale) {return null;}

 
 
public Matrix4 toNormalMatrix () {return null;}

 
public String toString () {return null;}

 
 
public static void mul (float[] mata, float[] matb) {}

 public static void mulVec (float[] mat, float[] vec) {}

 public static void prj (float[] mat, float[] vec) {}

 public static void rot (float[] mat, float[] vec) {}

 
 public static boolean inv (float[] values) {return false;}

 
 
public static float det (float[] values) {return 0.0f;}

 
 
public Matrix4 translate (Vector3 translation) {return null;}

 
 
public Matrix4 translate (float x, float y, float z) {return null;}

 
 
public Matrix4 rotate (Vector3 axis, float degrees) {return null;}

 
 
public Matrix4 rotateRad (Vector3 axis, float radians) {return null;}

 
 
public Matrix4 rotate (float axisX, float axisY, float axisZ, float degrees) {return null;}

 
 
public Matrix4 rotateRad (float axisX, float axisY, float axisZ, float radians) {return null;}

 
 
public Matrix4 rotate (Quaternion rotation) {return null;}

 
 
public Matrix4 rotate ( Vector3 v1,  Vector3 v2) {return null;}

 
 
public Matrix4 rotateTowardDirection ( Vector3 direction,  Vector3 up) {return null;}

 
 
public Matrix4 rotateTowardTarget ( Vector3 target,  Vector3 up) {return null;}

 
 
public Matrix4 scale (float scaleX, float scaleY, float scaleZ) {return null;}

 
 
public void extract4x3Matrix (float[] dst) {}

 
 public boolean hasRotationOrScaling () {return false;}

}
