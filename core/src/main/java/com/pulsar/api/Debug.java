package com.pulsar.api;

import com.badlogic.gdx.utils.Array;
import com.pulsar.api.math.MathUtils;
import space.earlygrey.shapedrawer.JoinType;

public class Debug {

    private static Array<String> console;

    public static float debugScale;

    public static void init() {
        console = new Array<>();
    }

    public static void log(int i) {
        log(String.valueOf(i));
    }

    public static void log(float f) {
        log(String.valueOf(f));
    }

    public static void log(String s) {
        console.add("[OUT]" + s);
    }

    public static void log(boolean b) {
        log(String.valueOf(b));
    }

    public static void log(Object obj) {
        log(String.valueOf(obj));
    }

    public static void error(String s) {
        console.add("[ERR]" + s);
    }

    public static Array<String> readDebug() {
        return console;
    }

    public static void line(float x0, float y0, float x1, float y1, com.pulsar.api.graphics.Color color, float strokeWidth) {
        com.pulsar.api.Renderer.drawer.setColor(color.r, color.g, color.b, color.a);
        com.pulsar.api.Renderer.drawer.line(x0, y0, x1, y1, debugScale * strokeWidth);
        com.pulsar.api.Renderer.drawer.setColor(1, 1, 1, 1);
    }

    public static void rectangle(float x, float y, float width, float height, float rotation, com.pulsar.api.graphics.Color color, float strokeWidth) {
        com.pulsar.api.Renderer.drawer.setColor(color.r, color.g, color.b, color.a);
        com.pulsar.api.Renderer.drawer.rectangle(x - width / 2f, y - height / 2f, width, height, debugScale * strokeWidth, rotation * MathUtils.degToRad);
        com.pulsar.api.Renderer.drawer.setColor(1, 1, 1, 1);
    }

    public static void circle(float x, float y, float radius, com.pulsar.api.graphics.Color color, float strokeWidth) {
        com.pulsar.api.Renderer.drawer.setColor(color.r, color.g, color.b, color.a);
        com.pulsar.api.Renderer.drawer.circle(x, y, radius, debugScale * strokeWidth);
        com.pulsar.api.Renderer.drawer.setColor(1, 1, 1, 1);
    }

    public static void polygon(float[] vertices, com.pulsar.api.graphics.Color color, float strokeWidth) {
        com.pulsar.api.Renderer.drawer.setColor(color.r, color.g, color.b, color.a);
        com.pulsar.api.Renderer.drawer.polygon(vertices, debugScale * strokeWidth, JoinType.SMOOTH);
        com.pulsar.api.Renderer.drawer.setColor(1, 1, 1, 1);
    }

}
