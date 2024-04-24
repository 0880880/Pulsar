package com.pulsar.api.math;

public class MathUtils {

 public static  float PI = 3.1415927410125732421875f;
 public static  float FLOAT_ROUNDING_ERROR = 0.000001f;
 public static  float HALF_PI = PI / 2f;
 public static  float PI2 = PI * 2f;

 public static  float E = (float) Math.E;

 public static  float radiansToDegrees = 180f * (1 / PI);
 public static  float degreesToRadians = (1 / 180f) * PI;
 public static  float radToDeg = 180f * (1 / PI);
 public static  float degToRad = (1 / 180f) * PI;

 public static float radToDeg(float val) {return 0.0f;}
 
public static float degToRad(float val) {return 0.0f;}

 
private MathUtils() {}

 

 public static int min(int a, int b) {return 0;}

 
public static float min(float a, float b) {return 0.0f;}

 
public static double min(double a, double b) {return 0.0;}

 
public static long min(long a, long b) {return 0L;}

 

 
public static int max(int a, int b) {return 0;}

 
public static float max(float a, float b) {return 0.0f;}

 
public static double max(double a, double b) {return 0.0;}

 
public static long max(long a, long b) {return 0L;}

 
public static float sin(float x) {return 0.0f;}

 
public static float cos(float x) {return 0.0f;}

 
public static float tan(float x) {return 0.0f;}

 
public static float atan(float x) {return 0.0f;}

 
public static float atan2(float y, float x) {return 0.0f;}

 
public static float sinDeg(float x) {return 0.0f;}

 
public static float cosDeg(float x) {return 0.0f;}

 
public static float tanDeg(float x) {return 0.0f;}

 
public static float atanDeg(float x) {return 0.0f;}

 
public static float atan2Deg(float y, float x) {return 0.0f;}

 

 
public static int clamp(int value, int min, int max) {return 0;}

 
public static long clamp(long value, long min, long max) {return 0L;}

 
public static float clamp(float value, float min, float max) {return 0.0f;}

 
public static double clamp(double value, double min, double max) {return 0.0;}

 

 
static public float lerp(float fromValue, float toValue, float progress) {return 0.0f;}

 

 
static private  int BIG_ENOUGH_INT = 16 * 1024;
 static private  double BIG_ENOUGH_FLOOR = BIG_ENOUGH_INT;
 static private  double BIG_ENOUGH_ROUND = BIG_ENOUGH_INT + 0.5f;

 public static int floor(float value) {return 0;}

 
public static int ceil(float value) {return 0;}

 
public static int round(float value) {return 0;}

 
public static float pow(float a, float b) {return 0.0f;}

 
public static int abs(int a) {return 0;}

 
public static float abs(float a) {return 0.0f;}

 
public static boolean isEqual(float a, float b) {return false;}
 
static public boolean isEqual (float a, float b, float tolerance) {return false;}

 
public static boolean isZero(float value) {return false;}

 
static public boolean isZero (float value, float tolerance) {return false;}



}
