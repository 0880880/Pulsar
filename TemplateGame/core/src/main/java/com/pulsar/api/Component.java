package com.pulsar.api;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.pulsar.api.components.Transform;
import com.pulsar.api.physics.Collision;

public abstract class Component {

    String className;
    Engine engine;

    public GameObject gameObject;

    public Array<Field> fields = new Array<>();

    private static String getPackageName(Class<?> cls) {
        String name = cls.getName();
        return name.substring(0, Math.max(0,name.length() - ClassReflection.getSimpleName(cls).length() - 1));
    }

    private static String componentsPackageName = getPackageName(Transform.class);

    public static boolean isComponentBuiltin(Class<?> componentClass) {
        return getPackageName(componentClass).startsWith(componentsPackageName);
    }

    void initialize(Engine engine) {
        this.engine = engine;
        for (Field field : ClassReflection.getFields(this.getClass())) {
            if (!field.getName().equals("className") && !field.getName().equals("engine") && !field.getName().equals("gameObject") && !field.getName().equals("fields"))
                fields.add(field);
        }
    }

    public void start() {}

    public void awake() {}

    public void update() {}

    public void debugUpdate() {}

    public void fixedUpdate() {}

    public void lateUpdate() {}

    public void onEnterCollision(Collision collision) {}

    public void onLeaveCollision(Collision collision) {}

}

