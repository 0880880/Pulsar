package com.pulsar.api.math;


import java.io.Serializable;


public class Quaternion implements Serializable {
 private static  long serialVersionUID = -7661875440774897168L;
 private static Quaternion tmp1 = new Quaternion(0, 0, 0, 0);
 private static Quaternion tmp2 = new Quaternion(0, 0, 0, 0);

 public float x;
 public float y;
 public float z;
 public float w;

 
 public Quaternion (float x, float y, float z, float w) {}

 public Quaternion () {}

 
 public Quaternion (Quaternion quaternion) {}

 
 public Quaternion (Vector3 axis, float angle) {}

 
 public Quaternion set (float x, float y, float z, float w) {return null;}

 
 
public Quaternion set (Quaternion quaternion) {return null;}

 
 
public Quaternion set (Vector3 axis, float angle) {return null;}

 
 
public Quaternion cpy () {return null;}

 
 
public  static float len ( float x,  float y,  float z,  float w) {return 0.0f;}

 
 
public float len () {return 0.0f;}

 
@Override
 public String toString () {return null;}

 
 
public Quaternion setEulerAngles (float yaw, float pitch, float roll) {return null;}

 
 
public Quaternion setEulerAnglesRad (float yaw, float pitch, float roll) {return null;}

 
 
public int getGimbalPole () {return 0;}

 
 
public float getRollRad () {return 0.0f;}

 
 
public float getRoll () {return 0.0f;}

 
 
public float getPitchRad () {return 0.0f;}

 
 
public float getPitch () {return 0.0f;}

 
 
public float getYawRad () {return 0.0f;}

 
 
public float getYaw () {return 0.0f;}

 
public  static float len2 ( float x,  float y,  float z,  float w) {return 0.0f;}

 
 
public float len2 () {return 0.0f;}

 
 
public Quaternion nor () {return null;}

 
 
public Quaternion conjugate () {return null;}

 
 
 
public Vector3 transform (Vector3 v) {return null;}

 
 
public Quaternion mul ( Quaternion other) {return null;}

 
 
public Quaternion mul ( float x,  float y,  float z,  float w) {return null;}

 
 
public Quaternion mulLeft (Quaternion other) {return null;}

 
 
public Quaternion mulLeft ( float x,  float y,  float z,  float w) {return null;}

 
 
public Quaternion add (Quaternion quaternion) {return null;}

 
 
public Quaternion add (float qx, float qy, float qz, float qw) {return null;}

 

 
 
public void toMatrix ( float[] matrix) {}

 
 public Quaternion idt () {return null;}

 
 
public boolean isIdentity () {return false;}

 
 
public boolean isIdentity ( float tolerance) {return false;}

 
 
 
public Quaternion setFromAxis ( Vector3 axis,  float degrees) {return null;}

 
 
public Quaternion setFromAxisRad ( Vector3 axis,  float radians) {return null;}

 
 
public Quaternion setFromAxis ( float x,  float y,  float z,  float degrees) {return null;}

 
 
public Quaternion setFromAxisRad ( float x,  float y,  float z,  float radians) {return null;}

 
 
public Quaternion setFromMatrix (boolean normalizeAxes, Matrix4 matrix) {return null;}

 
 
public Quaternion setFromMatrix (Matrix4 matrix) {return null;}

 
 
public Quaternion setFromMatrix (boolean normalizeAxes, Matrix3 matrix) {return null;}

 
 
public Quaternion setFromMatrix (Matrix3 matrix) {return null;}

 
 
public Quaternion setFromAxes (float xx, float xy, float xz, float yx, float yy, float yz, float zx, float zy, float zz) {return null;}

 
 
public Quaternion setFromAxes (boolean normalizeAxes, float xx, float xy, float xz, float yx, float yy, float yz, float zx,
 float zy, float zz) {return null;}

 
 
public Quaternion setFromCross ( Vector3 v1,  Vector3 v2) {return null;}

 
 
public Quaternion setFromCross ( float x1,  float y1,  float z1,  float x2,  float y2,
  float z2) {return null;}

 
 
public Quaternion slerp (Quaternion end, float alpha) {return null;}

 
 
public Quaternion slerp (Quaternion[] q) {return null;}

 
 
public Quaternion slerp (Quaternion[] q, float[] w) {return null;}

 
 
public Quaternion exp (float alpha) {return null;}

 
@Override
 public int hashCode () {return 0;}

 
@Override
 public boolean equals (Object obj) {return false;}

 
 
public  static float dot ( float x1,  float y1,  float z1,  float w1,  float x2,  float y2,
  float z2,  float w2) {return 0.0f;}

 
 
public float dot ( Quaternion other) {return 0.0f;}

 
 
public float dot ( float x,  float y,  float z,  float w) {return 0.0f;}

 
 
public Quaternion mul (float scalar) {return null;}

 
 
public float getAxisAngle (Vector3 axis) {return 0.0f;}

 
 
public float getAxisAngleRad (Vector3 axis) {return 0.0f;}

 
 
public float getAngleRad () {return 0.0f;}

 
 
public float getAngle () {return 0.0f;}

 
 
public void getSwingTwist ( float axisX,  float axisY,  float axisZ,  Quaternion swing,
  Quaternion twist) {}

 
 public void getSwingTwist ( Vector3 axis,  Quaternion swing,  Quaternion twist) {}

 
 public float getAngleAroundRad ( float axisX,  float axisY,  float axisZ) {return 0.0f;}

 
 
public float getAngleAroundRad ( Vector3 axis) {return 0.0f;}

 
 
public float getAngleAround ( float axisX,  float axisY,  float axisZ) {return 0.0f;}

 
 
public float getAngleAround ( Vector3 axis) {return 0.0f;}

}
