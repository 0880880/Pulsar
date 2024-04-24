package com.pulsar.api.graphics;

public class Color {

 public float r = 0;
 public float g = 0;
 public float b = 0;
 public float a = 1;

 public static  Color WHITE = new Color(1,1,1,1);
 public static  Color BLACK = new Color(0,0,0,1);
 public static  Color RED = new Color(1,0,0,1);
 public static  Color GREEN = new Color(0,1,0,1);
 public static  Color BLUE = new Color(0,0,1,1);
 public static  Color CLEAR = new Color(1,1,1,0);

 public Color() {}

 public Color(float r, float g, float b, float a) {}

 public Color(Color color) {}

 public Color set(float r, float g, float b, float a) {return null;}

 
public Color set(Color color) {return null;}

 
public Color add(float r, float g, float b, float a) {return null;}

 
public Color add(Color color) {return null;}

 
public Color sub(float r, float g, float b, float a) {return null;}

 
public Color sub(Color color) {return null;}

 
public Color scl(float r, float g, float b, float a) {return null;}

 
public Color scl(Color color) {return null;}

 
public Color scl(float scalar) {return null;}

 
public Color clamp () {return null;}

 
public Color lerp ( Color target,  float t) {return null;}

 
public Color lerp ( float r,  float g,  float b,  float a,  float t) {return null;}


}
