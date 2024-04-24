package com.pulsar.api;

import com.badlogic.gdx.Gdx;
import com.pulsar.api.components.Camera;
import com.pulsar.api.math.Vector2;

import java.util.HashMap;

public class Input {

    private static HashMap<String, Integer> keyMapping = new HashMap<>();
    private static HashMap<String, Integer> buttonMapping = new HashMap<>();

    private static Vector2 mousePosition = new Vector2();
    private static com.badlogic.gdx.math.Vector2 gMousePosition = new com.badlogic.gdx.math.Vector2();

    static void init() {

        keyMapping.put("A", com.badlogic.gdx.Input.Keys.A);
        keyMapping.put("B", com.badlogic.gdx.Input.Keys.B);
        keyMapping.put("C", com.badlogic.gdx.Input.Keys.C);
        keyMapping.put("D", com.badlogic.gdx.Input.Keys.D);
        keyMapping.put("E", com.badlogic.gdx.Input.Keys.E);
        keyMapping.put("F", com.badlogic.gdx.Input.Keys.F);
        keyMapping.put("G", com.badlogic.gdx.Input.Keys.G);
        keyMapping.put("H", com.badlogic.gdx.Input.Keys.H);
        keyMapping.put("I", com.badlogic.gdx.Input.Keys.I);
        keyMapping.put("J", com.badlogic.gdx.Input.Keys.J);
        keyMapping.put("K", com.badlogic.gdx.Input.Keys.K);
        keyMapping.put("L", com.badlogic.gdx.Input.Keys.L);
        keyMapping.put("M", com.badlogic.gdx.Input.Keys.M);
        keyMapping.put("N", com.badlogic.gdx.Input.Keys.N);
        keyMapping.put("O", com.badlogic.gdx.Input.Keys.O);
        keyMapping.put("P", com.badlogic.gdx.Input.Keys.P);
        keyMapping.put("Q", com.badlogic.gdx.Input.Keys.Q);
        keyMapping.put("R", com.badlogic.gdx.Input.Keys.R);
        keyMapping.put("S", com.badlogic.gdx.Input.Keys.S);
        keyMapping.put("T", com.badlogic.gdx.Input.Keys.T);
        keyMapping.put("U", com.badlogic.gdx.Input.Keys.U);
        keyMapping.put("V", com.badlogic.gdx.Input.Keys.V);
        keyMapping.put("W", com.badlogic.gdx.Input.Keys.W);
        keyMapping.put("X", com.badlogic.gdx.Input.Keys.X);
        keyMapping.put("Y", com.badlogic.gdx.Input.Keys.Y);
        keyMapping.put("Z", com.badlogic.gdx.Input.Keys.Z);
        keyMapping.put("0", com.badlogic.gdx.Input.Keys.NUM_0);
        keyMapping.put("1", com.badlogic.gdx.Input.Keys.NUM_1);
        keyMapping.put("2", com.badlogic.gdx.Input.Keys.NUM_2);
        keyMapping.put("3", com.badlogic.gdx.Input.Keys.NUM_3);
        keyMapping.put("4", com.badlogic.gdx.Input.Keys.NUM_4);
        keyMapping.put("5", com.badlogic.gdx.Input.Keys.NUM_5);
        keyMapping.put("6", com.badlogic.gdx.Input.Keys.NUM_6);
        keyMapping.put("7", com.badlogic.gdx.Input.Keys.NUM_7);
        keyMapping.put("8", com.badlogic.gdx.Input.Keys.NUM_8);
        keyMapping.put("9", com.badlogic.gdx.Input.Keys.NUM_9);
        keyMapping.put("SPACE", com.badlogic.gdx.Input.Keys.SPACE);
        keyMapping.put("SHIFT_LEFT", com.badlogic.gdx.Input.Keys.SHIFT_LEFT);
        keyMapping.put("SHIFT_RIGHT", com.badlogic.gdx.Input.Keys.SHIFT_RIGHT);
        keyMapping.put("CONTROL_LEFT", com.badlogic.gdx.Input.Keys.CONTROL_LEFT);
        keyMapping.put("CONTROL_RIGHT", com.badlogic.gdx.Input.Keys.CONTROL_RIGHT);
        keyMapping.put("ALT_LEFT", com.badlogic.gdx.Input.Keys.ALT_LEFT);
        keyMapping.put("ALT_RIGHT", com.badlogic.gdx.Input.Keys.ALT_RIGHT);
        keyMapping.put("ESCAPE", com.badlogic.gdx.Input.Keys.ESCAPE);
        keyMapping.put("ENTER", com.badlogic.gdx.Input.Keys.ENTER);
        keyMapping.put("F1", com.badlogic.gdx.Input.Keys.F1);
        keyMapping.put("F2", com.badlogic.gdx.Input.Keys.F2);
        keyMapping.put("F3", com.badlogic.gdx.Input.Keys.F3);
        keyMapping.put("F4", com.badlogic.gdx.Input.Keys.F4);
        keyMapping.put("F5", com.badlogic.gdx.Input.Keys.F5);
        keyMapping.put("F6", com.badlogic.gdx.Input.Keys.F6);
        keyMapping.put("F7", com.badlogic.gdx.Input.Keys.F7);
        keyMapping.put("F8", com.badlogic.gdx.Input.Keys.F8);
        keyMapping.put("F9", com.badlogic.gdx.Input.Keys.F9);
        keyMapping.put("F10", com.badlogic.gdx.Input.Keys.F10);
        keyMapping.put("F11", com.badlogic.gdx.Input.Keys.F11);
        keyMapping.put("F12", com.badlogic.gdx.Input.Keys.F12);
        keyMapping.put("F13", com.badlogic.gdx.Input.Keys.F13);
        keyMapping.put("F14", com.badlogic.gdx.Input.Keys.F14);
        keyMapping.put("F15", com.badlogic.gdx.Input.Keys.F15);
        keyMapping.put("F16", com.badlogic.gdx.Input.Keys.F16);
        keyMapping.put("F17", com.badlogic.gdx.Input.Keys.F17);
        keyMapping.put("F18", com.badlogic.gdx.Input.Keys.F18);
        keyMapping.put("F19", com.badlogic.gdx.Input.Keys.F19);
        keyMapping.put("F20", com.badlogic.gdx.Input.Keys.F20);
        keyMapping.put("F21", com.badlogic.gdx.Input.Keys.F21);
        keyMapping.put("F22", com.badlogic.gdx.Input.Keys.F22);
        keyMapping.put("F23", com.badlogic.gdx.Input.Keys.F23);
        keyMapping.put("F24", com.badlogic.gdx.Input.Keys.F24);
        keyMapping.put("BACKSPACE", com.badlogic.gdx.Input.Keys.BACKSPACE);
        keyMapping.put("SLASH", com.badlogic.gdx.Input.Keys.SLASH);
        keyMapping.put("BACKSLASH", com.badlogic.gdx.Input.Keys.BACKSLASH);
        keyMapping.put("LEFT", com.badlogic.gdx.Input.Keys.LEFT);
        keyMapping.put("RIGHT", com.badlogic.gdx.Input.Keys.RIGHT);
        keyMapping.put("UP", com.badlogic.gdx.Input.Keys.UP);
        keyMapping.put("DOWN", com.badlogic.gdx.Input.Keys.DOWN);
        keyMapping.put("CAPSLOCK", com.badlogic.gdx.Input.Keys.CAPS_LOCK);
        keyMapping.put("TAB", com.badlogic.gdx.Input.Keys.TAB);
        keyMapping.put("MINUS", com.badlogic.gdx.Input.Keys.MINUS);
        keyMapping.put("PLUS", com.badlogic.gdx.Input.Keys.PLUS);

        buttonMapping.put("LEFT", 0);
        buttonMapping.put("RIGHT", 1);
        buttonMapping.put("MIDDLE", 2);
        buttonMapping.put("BACK", 3);
        buttonMapping.put("FORWARD", 4);

    }

    public static boolean isKeyPressed(String key) {
        if (keyMapping.containsKey(key.toUpperCase()))
            return Gdx.input.isKeyPressed(keyMapping.get(key));
        return false;
    }

    public static boolean isKeyJustPressed(String key) {
        if (keyMapping.containsKey(key.toUpperCase()))
            return Gdx.input.isKeyJustPressed(keyMapping.get(key));
        return false;
    }

    public static boolean isButtonPressed(String button) {
        if (buttonMapping.containsKey(button.toUpperCase()))
            return Gdx.input.isButtonPressed(buttonMapping.get(button));
        return false;
    }

    public static boolean isButtonJustPressed(String button) {
        if (buttonMapping.containsKey(button.toUpperCase()))
            return Gdx.input.isButtonJustPressed(buttonMapping.get(button));
        return false;
    }

    static void update(Camera camera) {
        gMousePosition.set(Gdx.input.getX(), Gdx.input.getY());
        gMousePosition.set(camera.getViewport().unproject(gMousePosition));
        mousePosition.set(gMousePosition.x, gMousePosition.y);
    }

    public static Vector2 getMouse() {
        return mousePosition.set(Gdx.input.getX(), Gdx.input.getY());
    }

}

