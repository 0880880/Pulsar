package com.pulsar.api;

import com.pulsar.api.math.MathUtils;

public class Debug {


 public static float debugScale;

 public static void init() {}

 public static void log(int i) {}

 public static void log(float f) {}

 public static void log(String s) {}

 public static void log(boolean b) {}

 public static void log(Object obj) {}

 public static void error(String s) {}


 public static void line(float x0, float y0, float x1, float y1, com.pulsar.api.graphics.Color color, float strokeWidth) {}

 public static void rectangle(float x, float y, float width, float height, float rotation, com.pulsar.api.graphics.Color color, float strokeWidth) {}

 public static void circle(float x, float y, float radius, com.pulsar.api.graphics.Color color, float strokeWidth) {}

 public static void polygon(float[] vertices, com.pulsar.api.graphics.Color color, float strokeWidth) {}

}
