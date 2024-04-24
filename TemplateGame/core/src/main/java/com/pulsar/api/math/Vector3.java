package com.pulsar.api.math;

public class Vector3 {

    public static  Vector3 Zero = new Vector3(0,0,0);
    public static  Vector3 X = new Vector3(1,0,0);
    public static  Vector3 Y = new Vector3(0,1,0);
    public static  Vector3 Z = new Vector3(0,0,1);

    public float x;
    public float y;
    public float z;

    public Vector3() {}

    public Vector3(float x, float y, float z) {
        set(x,y,z);
    }

    public Vector3(Vector3 v) {
        set(v);
    }

    public Vector3 set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3 set(Vector3 v) {
        set(v.x, v.y, v.z);
        return this;
    }

    public Vector3 setZero() {
        set(Zero);
        return this;
    }

    public Vector3 add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3 add(Vector3 v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    public Vector3 sub(float x, float y, float z) {
        return add(-x, -y, -z);
    }

    public Vector3 sub(Vector3 v) {
        return add(-v.x, -v.y, -v.z);
    }

    public Vector3 scl(float scalar) {
        return set(x * scalar, y * scalar, z * scalar);
    }

    public static float len( float x,  float y,  float z) {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    public float len() {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    public static float len2( float x,  float y,  float z) {
        return x * x + y * y + z * z;
    }

    public float len2() {
        return x * x + y * y + z * z;
    }

    public static float dst( float x1,  float y1,  float z1,  float x2,  float y2,  float z2) {
         float a = x2 - x1;
         float b = y2 - y1;
         float c = z2 - z1;
        return (float)Math.sqrt(a * a + b * b + c * c);
    }

    public float dst( Vector3 vector) {
         float a = vector.x - x;
         float b = vector.y - y;
         float c = vector.z - z;
        return (float)Math.sqrt(a * a + b * b + c * c);
    }

    public float dst(float x, float y, float z) {
         float a = x - this.x;
         float b = y - this.y;
         float c = z - this.z;
        return (float)Math.sqrt(a * a + b * b + c * c);
    }

    public static float dst2( float x1,  float y1,  float z1,  float x2,  float y2,  float z2) {
         float a = x2 - x1;
         float b = y2 - y1;
         float c = z2 - z1;
        return a * a + b * b + c * c;
    }

    public float dst2(Vector3 point) {
         float a = point.x - x;
         float b = point.y - y;
         float c = point.z - z;
        return a * a + b * b + c * c;
    }

    public float dst2(float x, float y, float z) {
         float a = x - this.x;
         float b = y - this.y;
         float c = z - this.z;
        return a * a + b * b + c * c;
    }

    public Vector3 nor() {
         float len2 = this.len2();
        if (len2 == 0f || len2 == 1f) return this;
        return this.scl(1f / (float)Math.sqrt(len2));
    }

    public static float dot(float x1, float y1, float z1, float x2, float y2, float z2) {
        return x1 * x2 + y1 * y2 + z1 * z2;
    }

    public float dot( Vector3 vector) {
        return x * vector.x + y * vector.y + z * vector.z;
    }

    public float dot(float x, float y, float z) {
        return this.x * x + this.y * y + this.z * z;
    }

    public Vector3 crs( Vector3 vector) {
        return this.set(y * vector.z - z * vector.y, z * vector.x - x * vector.z, x * vector.y - y * vector.x);
    }

    public Vector3 crs(float x, float y, float z) {
        return this.set(this.y * z - this.z * y, this.z * x - this.x * z, this.x * y - this.y * x);
    }

    public boolean equals(Object obj) {
        if (obj.getClass().equals(Vector3.class)) {
            Vector3 v = (Vector3) obj;
            return x == v.x && y == v.y;
        } else {
            return false;
        }
    }

    public Vector3 cpy() {
        return new Vector3(this);
    }

}

