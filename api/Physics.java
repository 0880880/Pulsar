package com.pulsar.api;

import com.pulsar.api.math.Vector2;

import java.io.PrintStream;

public class Physics {

    private static Vector2 gravity = new Vector2(0, -9.81f);
    private static int velocityIterations = 6;
    private static int positionIterations = 2;

    static void start() {}

    static void stop() {}

    static void step(float timeStep) {}



    public static int getVelocityIterations() {return 0;}

    public static void setVelocityIterations(int velocityIterations) {}

    public static int getPositionIterations() {return 0;}

    public static void setPositionIterations(int positionIterations) {}
}

