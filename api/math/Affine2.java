package com.pulsar.api.math;


import java.io.Serializable;


public  class Affine2 implements Serializable {
 private static  long serialVersionUID = 1524569123485049187L;

 public float m00 = 1, m01 = 0, m02 = 0;
 public float m10 = 0, m11 = 1, m12 = 0;

 

 
 public Affine2 () {}

 
 public Affine2 (Affine2 other) {}

 
 public Affine2 idt () {return null;}

 
 
public Affine2 set (Affine2 other) {return null;}

 
 
public Affine2 set (Matrix3 matrix) {return null;}

 
 
public Affine2 set (Matrix4 matrix) {return null;}

 
 
public Affine2 setToTranslation (float x, float y) {return null;}

 
 
public Affine2 setToTranslation (Vector2 trn) {return null;}

 
 
public Affine2 setToScaling (float scaleX, float scaleY) {return null;}

 
 
public Affine2 setToScaling (Vector2 scale) {return null;}

 
 
public Affine2 setToRotation (float degrees) {return null;}

 
 
public Affine2 setToRotationRad (float radians) {return null;}

 
 
public Affine2 setToRotation (float cos, float sin) {return null;}

 
 
public Affine2 setToShearing (float shearX, float shearY) {return null;}

 
 
public Affine2 setToShearing (Vector2 shear) {return null;}

 
 
public Affine2 setToTrnRotScl (float x, float y, float degrees, float scaleX, float scaleY) {return null;}

 
 
public Affine2 setToTrnRotScl (Vector2 trn, float degrees, Vector2 scale) {return null;}

 
 
public Affine2 setToTrnRotRadScl (float x, float y, float radians, float scaleX, float scaleY) {return null;}

 
 
public Affine2 setToTrnRotRadScl (Vector2 trn, float radians, Vector2 scale) {return null;}

 
 
public Affine2 setToTrnScl (float x, float y, float scaleX, float scaleY) {return null;}

 
 
public Affine2 setToTrnScl (Vector2 trn, Vector2 scale) {return null;}

 
 
public Affine2 setToProduct (Affine2 l, Affine2 r) {return null;}

 
 
public Affine2 inv () {return null;}

 
 
public Affine2 mul (Affine2 other) {return null;}

 
 
public Affine2 preMul (Affine2 other) {return null;}

 
 
public Affine2 translate (float x, float y) {return null;}

 
 
public Affine2 translate (Vector2 trn) {return null;}

 
 
public Affine2 preTranslate (float x, float y) {return null;}

 
 
public Affine2 preTranslate (Vector2 trn) {return null;}

 
 
public Affine2 scale (float scaleX, float scaleY) {return null;}

 
 
public Affine2 scale (Vector2 scale) {return null;}

 
 
public Affine2 preScale (float scaleX, float scaleY) {return null;}

 
 
public Affine2 preScale (Vector2 scale) {return null;}

 
 
public Affine2 rotate (float degrees) {return null;}

 
 
public Affine2 rotateRad (float radians) {return null;}

 
 
public Affine2 preRotate (float degrees) {return null;}

 
 
public Affine2 preRotateRad (float radians) {return null;}

 
 
public Affine2 shear (float shearX, float shearY) {return null;}

 
 
public Affine2 shear (Vector2 shear) {return null;}

 
 
public Affine2 preShear (float shearX, float shearY) {return null;}

 
 
public Affine2 preShear (Vector2 shear) {return null;}

 
 
public float det () {return 0.0f;}

 
 
public Vector2 getTranslation (Vector2 position) {return null;}

 
 
public boolean isTranslation () {return false;}

 
 
public boolean isIdt () {return false;}

 
 
public void applyTo (Vector2 point) {}

 @Override
 public String toString () {return null;}

}
