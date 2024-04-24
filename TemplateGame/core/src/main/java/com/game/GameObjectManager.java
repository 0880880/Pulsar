package com.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.pulsar.api.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.pulsar.api.*;
import com.pulsar.api.components.Camera;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import static com.game.Statics.json;


public class GameObjectManager {

    GameObject main;

    public static Engine engine;

    HashMap<String, Component> objectRefrences = new HashMap<>();
    HashMap<String, String> objectRefrenceFields = new HashMap<>();

    public GameObject createGameObject(SerializableGameObject object) {
        GameObject gameObject = new GameObject();
        gameObject.name = object.name;
        gameObject.ID = object.ID;
        for (SerializableGameObject child : object.children) {
            gameObject.children.add(createGameObject(child));
        }
        for (String componentName : object.components) {
            if (componentName.startsWith("builtin.")) {
                if (componentName.equals("builtin.Transform")) {
                    HashMap<String, Object> map = object.componentFields.get(componentName);
                    gameObject.transform.position = (com.pulsar.api.math.Vector2) map.get("position");
                    gameObject.transform.localPosition = (com.pulsar.api.math.Vector2) map.get("localPosition");
                    gameObject.transform.scale = (com.pulsar.api.math.Vector2) map.get("scale");
                    gameObject.transform.rotation = ((float) map.get("rotation"));
                    gameObject.addComponent(gameObject.transform);
                } else {
                    try {
                        Class<? extends Component> cls = (Class<? extends Component>) ClassReflection.forName("com.pulsar.api.components." + componentName.substring(8));

                        Component cmp = ClassReflection.newInstance(cls);

                        HashMap<String, Object> map = object.componentFields.get(componentName);

                        for (String fieldName : map.keySet()) {
                            for (com.badlogic.gdx.utils.reflect.Field f : ClassReflection.getFields(cmp.getClass())) {
                                if (f.getName().equals(fieldName)) {
                                    Class<?> type = f.getType();
                                    Object obj = map.get(fieldName);
                                    if (obj != null) {
                                        if (type == byte.class) {
                                            f.set(cmp, (byte) obj);
                                        } else if (type == short.class) {
                                            f.set(cmp, (short) obj);
                                        } else if (type == int.class) {
                                            f.set(cmp, (int) obj);
                                        } else if (type == float.class) {
                                            f.set(cmp, (float) obj);
                                        } else if (type == long.class) {
                                            f.set(cmp, (long) obj);
                                        } else if (type == Texture.class) {
                                            Texture tex = (Texture) obj;
                                            if (tex.textureAsset == null)
                                                tex.textureAsset = Renderer.getDefaultTexture().textureAsset;
                                            Texture newTex = new Texture(TextureManager.get(tex.textureAsset.getPath(), tex.textureAsset.getFilename()));
                                            newTex.uv0.set(tex.uv0);
                                            newTex.uv1.set(tex.uv1);
                                            f.set(cmp, newTex);
                                        } else {
                                            f.set(cmp, obj);
                                        }
                                    }
                                    break;
                                }
                            }
                        }

                        gameObject.addComponent(cmp);

                    } catch (ReflectionException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (componentName.startsWith("user.")) {
                try {
                    Class<? extends Component> cls = (Class<? extends Component>) ClassReflection.forName("com.game.scripts." + componentName.substring(5));

                    Component cmp = ClassReflection.newInstance(cls);

                    engine.initComponent(cmp);

                    HashMap<String, Object> map = object.componentFields.get(componentName);

                    for (String fieldName : map.keySet()) {
                        for (com.badlogic.gdx.utils.reflect.Field f : cmp.fields) {
                            if (f.getName().equals(fieldName)) {
                                Class<?> type = f.getType();
                                Object obj = map.get(fieldName);
                                if (obj != null) {
                                    if (type == byte.class) {
                                        f.set(cmp, (byte) obj);
                                    } else if (type == short.class) {
                                        f.set(cmp, (short) obj);
                                    } else if (type == int.class) {
                                        f.set(cmp, (int) obj);
                                    } else if (type == float.class) {
                                        f.set(cmp, (float) obj);
                                    } else if (type == long.class) {
                                        f.set(cmp, (long) obj);
                                    } else if (obj.getClass() == String.class) {
                                        String s = (String) obj;
                                        if (s.startsWith("cmp.") && type != String.class) {
                                            objectRefrences.put(s, cmp);
                                            objectRefrenceFields.put(s, fieldName);
                                        } else {
                                            f.set(cmp, s);
                                        }
                                    } else if (type == Texture.class) {
                                        Texture tex = (Texture) obj;
                                        if (tex.textureAsset == null)
                                            tex.textureAsset = Renderer.getDefaultTexture().textureAsset;
                                        Texture newTex = new Texture(TextureManager.get(tex.textureAsset.getPath(), tex.textureAsset.getFilename()));
                                        newTex.uv0.set(tex.uv0);
                                        newTex.uv1.set(tex.uv1);
                                        f.set(cmp, newTex);
                                    } else {
                                        f.set(cmp, obj);
                                    }
                                }
                                break;
                            }
                        }
                    }

                    gameObject.addComponent(cmp);

                } catch (ReflectionException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return gameObject;
    }

    public void fix(Array<GameObject> gameObjects) {
        for (String s : objectRefrences.keySet()) {
            try {
                Component cmp = objectRefrences.get(s);
                com.badlogic.gdx.utils.reflect.Field f = ClassReflection.getField(cmp.getClass(), objectRefrenceFields.get(s));
                long ID = Long.valueOf(s.substring(4, s.lastIndexOf(".")));
                for (GameObject g : gameObjects) {
                    if (g.ID == ID) {
                        String cmpName = s.substring(s.lastIndexOf(".") + 1);
                        Component target = g.getComponent((Class<? extends Component>) ClassReflection.forName("com.pulsar.api.components." + cmpName));
                        f.set(cmp, target);
                        break;
                    }
                }
            } catch (ReflectionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    Sprite sprite;
    CpuSpriteBatch batch;
    ShapeDrawer drawer;

    CameraHolder cameraHolder = new CameraHolder();

    boolean search(GameObject gameObject) {
        for (int i = 0; i < gameObject.components.size(); i++) {
            Component component = gameObject.components.get(i);
            if (component.getClass() == Camera.class) {
                cameraHolder.camera = gameObject.getComponent(Camera.class);
                return true;
            }
        }

        for (int i = 0; i < gameObject.children.size(); i++) {
            GameObject g = gameObject.children.get(i);
            boolean a = search(g);
            if (a) return a;
        }
        return false;
    }

    Array<GameObject> allGameObjects = new Array<>();

    public void listAll(GameObject parent) {
        allGameObjects.add(parent);
        for (GameObject g : parent.children) {
            listAll(g);
        }
    }

    public void load() {

        sprite = new Sprite();

        batch = new CpuSpriteBatch();

        Pixmap pixel = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixel.setColor(Color.WHITE);
        pixel.fill();

        drawer = new ShapeDrawer(batch, new TextureRegion(new com.badlogic.gdx.graphics.Texture(pixel)));
        engine = new Engine(sprite, drawer, cameraHolder);

        Statics.engine = engine;

        SerializableProject proj = json.fromJson(SerializableProject.class, Gdx.files.internal("main.json").readString());
        Statics.currentProject = proj.createProject();

        main = createGameObject(proj.rootGameObject);

        search(main);

        listAll(main);

        fix(allGameObjects);

    }

    public void start() {
        engine.start(main, false);
        Gdx.graphics.setTitle(Statics.currentProject.windowTitle);
        if (Statics.currentProject.fullscreen) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            Gdx.graphics.setWindowedMode((int) Statics.currentProject.windowSize.x, (int) Statics.currentProject.windowSize.y);
        }
    }

    public void update() {
        if (cameraHolder != null) {
            ScreenUtils.clear(cameraHolder.camera.backgroundColor.r, cameraHolder.camera.backgroundColor.g, cameraHolder.camera.backgroundColor.b, cameraHolder.camera.backgroundColor.a);
            cameraHolder.camera.apply(batch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Statics.mouse.set(cameraHolder.camera.getViewport().unproject(Statics.mouse.set(Gdx.input.getX(), Gdx.input.getY())));
        }

        batch.begin();
        engine.update(main);
        batch.end();
    }

}
