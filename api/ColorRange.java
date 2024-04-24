package com.pulsar.api;

import com.pulsar.api.graphics.Color;

import java.util.List;

public class ColorRange {


 public ColorRange() {}

 public ColorRange(float[] timeline, Color... colors) {}

 public void add(float t, Color color) {}

 public void remove(int index) {}

 public void insert(int index, float time, Color color) {}

 public float getTime(int index) {return 0.0f;}

 
public void setTime(int index, float time) {}

 public Color getColor(int index) {return null;}

 
public Color valueAt(Color color, float t) {return null;}

 
public int size() {return 0;}



}
