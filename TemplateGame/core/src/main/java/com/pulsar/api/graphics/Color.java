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

    public Color(float r, float g, float b, float a) {
        set(r, g, b, a);
    }

    public Color(Color color) {
        set(color);
    }

    public Color set(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return this;
    }

    public Color set(Color color) {
        return set(color.r, color.g, color.b, color.a);
    }

    public Color add(float r, float g, float b, float a) {
        this.r += r;
        this.g += g;
        this.b += b;
        this.a += a;
        return this;
    }

    public Color add(Color color) {
        return add(color.r, color.g, color.b, color.a);
    }

    public Color sub(float r, float g, float b, float a) {
        this.r -= r;
        this.g -= g;
        this.b -= b;
        this.a -= a;
        return this;
    }

    public Color sub(Color color) {
        return sub(color.r, color.g, color.b, color.a);
    }

    public Color scl(float r, float g, float b, float a) {
        this.r *= r;
        this.g *= g;
        this.b *= b;
        this.a *= a;
        return this;
    }

    public Color scl(Color color) {
        return scl(color.r, color.g, color.b, color.a);
    }

    public Color scl(float scalar) {
        return scl(scalar, scalar, scalar, scalar);
    }

    public Color clamp () {
        if (r < 0)
            r = 0;
        else if (r > 1) r = 1;

        if (g < 0)
            g = 0;
        else if (g > 1) g = 1;

        if (b < 0)
            b = 0;
        else if (b > 1) b = 1;

        if (a < 0)
            a = 0;
        else if (a > 1) a = 1;
        return this;
    }

    public Color lerp ( Color target,  float t) {
        return lerp(target.r, target.g, target.b, target.a, t);
    }

    public Color lerp ( float r,  float g,  float b,  float a,  float t) {
        this.r += t * (r - this.r);
        this.g += t * (g - this.g);
        this.b += t * (b - this.b);
        this.a += t * (a - this.a);
        return clamp();
    }

}

