package com.pulsar.api;

import com.pulsar.api.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class ColorRange {

    private ArrayList<Float> timeline = new ArrayList<>();
    private ArrayList<Color> colors = new ArrayList<>();

    public ColorRange() {
        add(0, new Color(Color.RED));
    }

    public ColorRange(float[] timeline, Color... colors) {
        if (timeline.length != colors.length) throw new RuntimeException("Timeline Length != Colors Length");
        for (float v : timeline) {
            this.timeline.add(v);
        }
        this.colors.addAll(List.of(colors));
    }

    public void add(float t, Color color) {
        this.timeline.add(t);
        this.colors.add(color);
    }

    public void remove(int index) {
        this.timeline.remove(index);
        this.colors.remove(index);
    }

    public void insert(int index, float time, Color color) {
        this.timeline.add(index, time);
        this.colors.add(index, color);
    }

    public float getTime(int index) {
        return this.timeline.get(index);
    }

    public void setTime(int index, float time) {
        this.timeline.set(index, time);
    }

    public Color getColor(int index) {
        return this.colors.get(index);
    }

    public Color valueAt(Color color, float t) {
        Color last = null;
        int lastIdx = 0;
        for (int i = 0; i < colors.size(); i++) {
            if (timeline.get(i) > t) break;
            last = colors.get(i);
            lastIdx = i;
        }
        if (last == null)
            return null;
        else if (lastIdx + 1 != size())
            return color.set(last).lerp(colors.get(lastIdx + 1), t);
        else
            return color.set(last);
    }

    public int size() {
        return this.colors.size();
    }

    public ArrayList<Color> getColors() {
        return this.colors;
    }

}

