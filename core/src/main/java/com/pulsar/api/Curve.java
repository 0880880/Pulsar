package com.pulsar.api;

import com.pulsar.api.math.MathUtils;
import com.pulsar.api.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Curve {

    private ArrayList<Vector2> points = new ArrayList<>();

    public  Curve() {
        this.points.add(new Vector2(0,0));
    }

    public Curve(Vector2... points) {
        this.points.addAll(List.of(points));
    }

    public void add(Vector2 point) {
        this.points.add(point);
    }

    public void remove(Vector2 point) {
        this.points.remove(point);
    }

    public void remove(int index) {
        this.points.remove(index);
    }

    public void insert(int index, Vector2 point) {
        this.points.add(index, point);
    }

    public Vector2 getPoint(int index) {
        return this.points.get(index);
    }

    public float valueAt(float t) {
        Vector2 last = null;
        int lastIdx = 0;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).x > t) break;
            last = points.get(i);
            lastIdx = i;
        }
        return last == null ? MathUtils.lerp(0,1,t) : MathUtils.lerp(last.y, lastIdx + 1 != size() ? points.get(lastIdx + 1).y : last.y, t);
    }

    public int size() {
        return this.points.size();
    }

    public ArrayList<Vector2> getPoints() {
        return this.points;
    }

}
