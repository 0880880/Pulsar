package com.pulsar.api;

import com.pulsar.api.math.MathUtils;
import com.pulsar.api.math.Vector2;

import java.util.List;

public class Curve {


 public Curve() {}

 public Curve(Vector2... points) {}

 public void add(Vector2 point) {}

 public void remove(Vector2 point) {}

 public void remove(int index) {}

 public void insert(int index, Vector2 point) {}

 public Vector2 getPoint(int index) {return null;}

 
public float valueAt(float t) {return 0.0f;}

 
public int size() {return 0;}



}
