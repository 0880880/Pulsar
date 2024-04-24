package com.pulsar.api.math;

public class Vector2 {

    public static final Vector2 Zero = new Vector2(0,0);
    public static final Vector2 X = new Vector2(1,0);
    public static final Vector2 Y = new Vector2(0,1);

    public float x;
    public float y;

    public Vector2() {}

    public Vector2(float x, float y) {
        set(x,y);
    }

    public Vector2(Vector2 v) {
        set(v);
    }

    public Vector2 set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2 set(Vector2 v) {
        set(v.x, v.y);
        return this;
    }

    public Vector2 setZero() {
        set(Zero);
        return this;
    }

    public Vector2 add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2 add(Vector2 v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public Vector2 sub(float x, float y) {
        return add(-x, -y);
    }

    public Vector2 sub(Vector2 v) {
        return add(-v.x, -v.y);
    }

    public Vector2 scl(float scalar) {
        return set(x * scalar, y * scalar);
    }

    public float len() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float len2() {
        return x * x + y * y;
    }

    public Vector2 nor() {
        float len = len();
        if (len != 0) {
            x /= len;
            y /= len;
        }
        return this;
    }

    public static float dot(float x1, float y1, float x2, float y2) {
        return x1 * x2 + y1 * y2;
    }

    public float dot(Vector2 v) {
        return x * v.x + y * v.y;
    }

    public float dot(float ox, float oy) {
        return x * ox + y * oy;
    }

    public static float dst (float x1, float y1, float x2, float y2) {
        final float x_d = x2 - x1;
        final float y_d = y2 - y1;
        return (float)Math.sqrt(x_d * x_d + y_d * y_d);
    }

    public float dst (Vector2 v) {
        final float x_d = v.x - x;
        final float y_d = v.y - y;
        return (float)Math.sqrt(x_d * x_d + y_d * y_d);
    }

    public float dst (float x, float y) {
        final float x_d = x - this.x;
        final float y_d = y - this.y;
        return (float)Math.sqrt(x_d * x_d + y_d * y_d);
    }

    public static float dst2 (float x1, float y1, float x2, float y2) {
        final float x_d = x2 - x1;
        final float y_d = y2 - y1;
        return x_d * x_d + y_d * y_d;
    }

    public float dst2 (Vector2 v) {
        final float x_d = v.x - x;
        final float y_d = v.y - y;
        return x_d * x_d + y_d * y_d;
    }

    public float dst2 (float x, float y) {
        final float x_d = x - this.x;
        final float y_d = y - this.y;
        return x_d * x_d + y_d * y_d;
    }

    public float crs (Vector2 v) {
        return this.x * v.y - this.y * v.x;
    }

    public float crs (float x, float y) {
        return this.x * y - this.y * x;
    }

    public float angleRad() {
        return (float)Math.atan2(y, x);
    }

    public float angleDeg() {
        float angle = MathUtils.radToDeg((float)Math.atan2(y, x));
        if (angle < 0) angle += 360;
        return angle;
    }

    public boolean equals(Object obj) {
        if (obj.getClass().equals(Vector2.class)) {
            Vector2 v = (Vector2) obj;
            return x == v.x && y == v.y;
        } else {
            return false;
        }
    }

    public Vector2 cpy() {
        return new Vector2(this);
    }

    public String toString() {
        return "( " + x + " , " + y + " )";
    }

}
