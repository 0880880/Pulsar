package com.pulsar.api;


public class Graphics {

 public enum Cursor {
 None, Arrow, Ibeam, Crosshair, Hand, HorizontalResize, VerticalResize, NWSEResize, NESWResize, AllResize, NotAllowed
 }

 private static float x;
 private static float y;

 private static int width;
 private static int height;

 static void init() {}

 public static int getWidth() {return 0;}

 
public static int getHeight() {return 0;}

 
public static void setWindowMode(int width, int height) {}

 public static void setFullscreenMode() {}

 public static boolean isFullscreen() {return false;}

 
public static void setTitle(String title) {}

 public static void setResizable(boolean resizable) {}

 public static void setUndecorated(boolean undecorated) {}

 public static void setVSync(boolean vsync) {}

 public static void setForegroundFPS(int fps) {}

 public static void setCursor(Cursor cursor) {}

 public void changeScene(String path) {}

}
